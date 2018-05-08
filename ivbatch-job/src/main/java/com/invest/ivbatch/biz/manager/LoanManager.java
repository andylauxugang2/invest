package com.invest.ivbatch.biz.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.invest.ivbatch.common.Constants;
import com.invest.ivbatch.mq.producer.LoanableDetailProducer;
import com.invest.ivcommons.redis.client.cacheclient.CacheClientHA;
import com.invest.ivcommons.rocketmq.model.LoanableDetailMessage;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivcommons.util.endecode.Base64Util;
import com.invest.ivcommons.util.serialize.HessianUtil;
import com.invest.ivcommons.util.thread.ThreadPoolUtils;
import com.invest.ivppad.biz.manager.PPDOpenApiLoanListManager;
import com.invest.ivppad.model.http.request.PPDOpenApiLoanListRequest;
import com.invest.ivppad.model.http.request.PPDOpenApiLoanListingDetailBatchRequest;
import com.invest.ivppad.model.http.response.PPDOpenApiLoanListResponse;
import com.invest.ivppad.model.http.response.PPDOpenApiLoanListingDetailBatchResponse;
import com.invest.ivppad.model.http.response.PPDOpenApiLoanListingDetailBatchResponse.LoanListingDetail;
import com.invest.ivppad.model.param.PPDOpenApiGetLoanListingDetailBatchParam;
import com.invest.ivppad.model.param.PPDOpenApiGetLoanListingStatusBatchParam;
import com.invest.ivppad.util.KeyVersionUtils;
import com.invest.ivppad.util.RedisKeyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by xugang on 2017/8/6.
 */
@Service
public class LoanManager {
    private static Logger logger = LoggerFactory.getLogger(LoanManager.class);

    @Resource(name = "ppdCacheClientHA")
    private CacheClientHA ppdCacheClientHA;

    @Autowired
    protected PPDOpenApiLoanListManager ppdOpenApiLoanListManager;

    @Resource
    private LoanableDetailProducer loanableDetailProducer;

    @Autowired
    private DistributedLockManager distributedLockManager;

    //TODO 配置中心大小
//    private ExecutorService downloadExcutorService = Executors.newWorkStealingPool(50); //并行级别为n 默认CPU数量 每时每刻只有n个线程同时执行
    private ExecutorService downloadExcutorService = ThreadPoolUtils.createBlockingPool(10, 20);
    //TODO 配置中心大小
    private ExecutorService filterHisLoanListExcutorService = ThreadPoolUtils.createBlockingPool(150, 500);
    private ExecutorService downloadDetailExcutorService = ThreadPoolUtils.createBlockingPool(150, 500);

    /**
     * 下载散标列表
     *
     * @param downloadPages
     * @param shardingItem
     * @param startDateTime 如果有则查询该时间之后的列表，精确到毫秒
     */
    public void downloadLoanList(int downloadPages, int shardingItem, Date startDateTime) {
        //设置每个分片负责下载的页数 如果当前分片为n 下载页数为 1-1*5 1*5-2*5 2*5-3*5 (n-1)*5-n*5
        int n = shardingItem + 1;
        int pageBegin = (n - 1) * downloadPages;
        if (pageBegin == 0) pageBegin = 1;
        int pageEnd = n * downloadPages;

        logger.debug("开始下载散标列表,起始页={},终止页={},shardingItem={}", pageBegin, pageEnd, shardingItem);

        //开启downloadPages个线程数并发下载
        for (int i = pageBegin; i <= pageEnd; i++) { //eg:1-100页
            final int index = i;
            downloadExcutorService.submit(() -> {
                PPDOpenApiLoanListRequest apiLoanListRequest = new PPDOpenApiLoanListRequest();
                apiLoanListRequest.setPageIndex(index);
                String startDateTimeParam = DateUtil.dateToString(startDateTime, DateUtil.DATE_FORMAT_DATETIME_FORMAT_MISECOND);
                //String startDateTimeParam = null;
                apiLoanListRequest.setStartDateTime(startDateTimeParam);
                long start = System.currentTimeMillis();
                ppdOpenApiLoanListManager.invokeLoanList(apiLoanListRequest, new DownListFutureCallback(index, start, startDateTime));
                return null;
            });

        }
    }

    class DownListFutureCallback implements FutureCallback<HttpResponse> {
        private int pageIndex;
        private long start; //如果有则查询该时间之后的列表，精确到毫秒
        private Date startDateTime;

        DownListFutureCallback(int pageIndex, long start, Date startDateTime) {
            this.pageIndex = pageIndex;
            this.start = start;
            this.startDateTime = startDateTime;
        }

        @Override
        public void completed(HttpResponse response) {
            try {
                String startTimeParam = DateUtil.dateToString(startDateTime, DateUtil.DATE_FORMAT_DATETIME_FORMAT_MISECOND);
                logger.debug("status=" + response.getStatusLine().getStatusCode());
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    logger.info("查询结果为null,status=" + response.getStatusLine().getStatusCode());
                    return;
                }
                String result = EntityUtils.toString(entity, "UTF-8");
                //logger.info("result=====" + result);
                ObjectMapper mapper = new ObjectMapper();
                PPDOpenApiLoanListResponse ppdOpenApiLoanListResponse = mapper.readValue(result, PPDOpenApiLoanListResponse.class);

                List<PPDOpenApiLoanListResponse.LoanInfo> loanInfoList = ppdOpenApiLoanListResponse.getLoanInfoList();

                if (CollectionUtils.isEmpty(loanInfoList)) {
                    logger.debug("该页无可投资标的列表,pageIndex={},startDateTime={}", pageIndex, startTimeParam);
                    return;
                }
                logger.info("下载单页散标列表完成,pageIndex={},startDateTime={},耗时={},loanInfoList={}", pageIndex, startTimeParam, System.currentTimeMillis() - start, loanInfoList.size());

                //多nio线程回调只有一个能获取锁写入redis

                //过滤历史下载标的缓存列表,如果缓存里有历史数据,表示已通知过用户投标 过滤掉
                List<Integer> needToLoanListingIds = filterHistoryDownLoanInfoList(pageIndex, loanInfoList);
                if (CollectionUtils.isEmpty(needToLoanListingIds)) {
                    logger.info("单页无新可投资标的列表,pageIndex={},startDateTime={}", pageIndex, startTimeParam);
                    return;
                }

                //获取标的详细信息
                doDownloadLoanDetailBatch(needToLoanListingIds, pageIndex);

            } catch (IOException e) {
                logger.error("处理散标列表结果发生异常", e);
            }
        }

        @Override
        public void failed(Exception e) {
            logger.error("下载单页散标列表完成失败,pageIndex=" + pageIndex, e);
        }

        @Override
        public void cancelled() {
            logger.info("cancelled");
        }
    }

    /*class DownLoanListCallable implements Callable<List<Integer>> {
        private int pageIndex;
        private Date startDateTime; //如果有则查询该时间之后的列表，精确到毫秒

        DownLoanListCallable(int pageIndex, Date startDateTime) {
            this.pageIndex = pageIndex;
            this.startDateTime = startDateTime;
        }

        @Override
        public List<Integer> call() {
//            List<Integer> listingIds = new ArrayList<>();
            StopWatch stopWatch = new StopWatch("单页下载并过滤散标列表");
            stopWatch.start("单页调用散标可投标列表接口");
            PPDOpenApiGetLoanListParam param = new PPDOpenApiGetLoanListParam();
            param.setPageIndex(pageIndex);
            if (startDateTime != null) {
                param.setStartDateTime(new DateTime(startDateTime.getTime()));
            }

            logger.debug("下载单页散标列表完成,pageIndex={}", pageIndex);
            //单页下载接口 接口返回200数据量
            PPDOpenApiLoanResult loanResult = loanService.getLoanList(param);
            stopWatch.stop();
            if (loanResult.isFailed()) {
                logger.error("获取散标可投标列表失败,error={}", loanResult);
                return null;
            }
            List<PPDOpenApiLoanListResponse.LoanInfo> loanInfoList = loanResult.getLoanInfoList();

            if (CollectionUtils.isEmpty(loanInfoList)) {
                logger.info("该页无可投资标的列表,pageIndex={}", pageIndex);
                return null;
            }

            logger.debug("下载单页散标列表完成,pageIndex={},startDateTime={},loanInfoList={}", pageIndex, startDateTime, loanInfoList.size());

            stopWatch.start("单页过滤历史下载标的列表");
            //过滤历史下载标的缓存列表,如果缓存里有历史数据,表示已通知过用户投标 过滤掉
            List<Integer> needToLoanListingIds = filterHistoryDownLoanInfoList(pageIndex, loanInfoList);
            stopWatch.stop();
            if (CollectionUtils.isEmpty(needToLoanListingIds)) {
                logger.debug("单页无新可投资标的列表,pageIndex={}", pageIndex);
                return null;
            }

            //获取标的详细信息
            doDownloadLoanDetailBatch(needToLoanListingIds);

            logger.info("总耗时统计 {}", stopWatch.prettyPrint());
            return needToLoanListingIds;
        }
    }*/

    //loanInfoList 当前标的列表一页数据量 2000 TODO 目前的每页查询所有redis标的列表 改为本地缓存方案 提高速度
    private List<Integer> filterHistoryDownLoanInfoList(int pageIndex, List<PPDOpenApiLoanListResponse.LoanInfo> loanInfoList) {
        Long start = System.currentTimeMillis();
        //获取分布式锁ListingId 防止重复过滤和投标
        String lockKey = "pageIndex" + pageIndex;
        boolean acLock = distributedLockManager.acquireDistributedLock(lockKey, Constants.DIS_LOCK_EXPIREINSECONDS_PAGEINDEX);
        logger.info("获取到分布式锁,过滤散标列表,bizKey=pageIndex{}", pageIndex);
        if (!acLock) {
            logger.info("未获取到分布式锁,不去过滤散标列表,bizKey=pageIndex{}", pageIndex);
            return null;
        }

        List<Integer> listingIds = new ArrayList<>(100);
        //分批过滤缓存历史下载散标列表
        int batchSize = PPDOpenApiGetLoanListingStatusBatchParam.FETCH_BATCH_STATUS_SIZE * 2;
        List<Callable<List<Integer>>> filterListingIdCallableList = new ArrayList<>(loanInfoList.size() / batchSize);

        List<Integer> listingIdsTemp = new ArrayList<>(batchSize);
        int counter = 0;
        for (PPDOpenApiLoanListResponse.LoanInfo loanInfo : loanInfoList) {
            //logger.info("loanInfo-listingId={}", loanInfo.getListingId());
            if (loanInfo.getRemainFunding() == 0) {
                //如果无可投资金额 则不去获取详情,省一次带宽
                continue;
            }
            listingIdsTemp.add(loanInfo.getListingId());
            counter++;
            //存够batchSize条发给下游线程处理
            if (counter % batchSize == 0) {
                filterListingIdCallableList.add(new FilterHisLoanListCallable(listingIdsTemp));
                listingIdsTemp = new ArrayList<>(batchSize);
            }
        }
        if (listingIdsTemp.size() > 0) {
            filterListingIdCallableList.add(new FilterHisLoanListCallable(listingIdsTemp));
        }

        //等待子线程执行结果
        try {
            List<Future<List<Integer>>> futureList = filterHisLoanListExcutorService.invokeAll(filterListingIdCallableList);
            for (Future<List<Integer>> future : futureList) {
                List<Integer> loanListingIdList = future.get();
                if (CollectionUtils.isEmpty(loanListingIdList)) {
                    continue;
                }
                //添加listingId
                listingIds.addAll(loanListingIdList.stream().collect(Collectors.toList()));
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("execution " + Throwables.getStackTraceAsString(e));
            Thread.currentThread().interrupt();
        }

        int size = 0;
        if (CollectionUtils.isNotEmpty(listingIds)) {
            size = listingIds.size();
            //批量放入缓存
            String key = KeyVersionUtils.rediskeyLoanDetailListV(RedisKeyUtils.keyLoanDetailList());
            //TODO 一个Hash 标太多 可以按照ListingId分片
            Map<String, byte[]> map = new HashMap<>();
            for (Integer listingId : listingIds) {
                //map.put(String.valueOf(detail.getListingId()), HessianUtil.toBytes(detail));
                map.put(String.valueOf(listingId), String.valueOf(listingId).getBytes());
            }
            ppdCacheClientHA.Hash().hmsetBit(key, map);
        }
        //释放锁 下一个thread过滤列表
        distributedLockManager.releaseDistributedLock(lockKey);
        logger.info("单页分批过滤缓存历史下载散标列表[{}]成功,afterSize={},pageIndex={},耗时={}", loanInfoList.size(), size, pageIndex, System.currentTimeMillis() - start);

        return listingIds;
    }

    private void doDownloadLoanDetailBatch(List<Integer> listingIds, int pageIndex) {
        //分批下载标的详情&通知策略任务
        int batchSize = PPDOpenApiGetLoanListingDetailBatchParam.FETCH_BATCH_DETAIL_SIZE;
        List<Integer> listingIdsTemp = new ArrayList<>(batchSize);
        int counter = 0;
        //单页下载散标详情 单个线程一次查询拍拍贷批量[10个标的详情]
        for (Integer listingId : listingIds) {
            listingIdsTemp.add(listingId);
            counter++;
            //存够batchSize条发给下游线程处理
            if (counter % batchSize == 0) {
                downloadDetailExcutorService.submit(new DownLoanDetailCallable(listingIdsTemp, pageIndex));
                listingIdsTemp = new ArrayList<>(batchSize);
            }

        }
        if (listingIdsTemp.size() > 0) {
            downloadDetailExcutorService.submit(new DownLoanDetailCallable(listingIdsTemp, pageIndex));
        }
    }

    class DownLoanDetailCallable implements Callable<List<LoanListingDetail>> {
        private List<Integer> listIds; //可投标列表页返回 ListingId
        private int pageIndex;

        DownLoanDetailCallable(List<Integer> listIds, int pageIndex) {
            this.listIds = listIds;
            this.pageIndex = pageIndex;
        }

        @Override
        public List<LoanListingDetail> call() throws Exception {
            if (CollectionUtils.isEmpty(listIds)) {
                return null;
            }

            //批量下载标的详情信息 一次接口返回10条数据 单次下载耗时为5000毫秒左右,多线程下载平均单次查询耗时为350毫秒
            //获取散标可投标列表
            PPDOpenApiLoanListingDetailBatchRequest loanListingDetailBatchRequest = new PPDOpenApiLoanListingDetailBatchRequest();
            loanListingDetailBatchRequest.setListIds(listIds);
            PPDOpenApiLoanListingDetailBatchResponse response = ppdOpenApiLoanListManager.getListingDetailBatch(loanListingDetailBatchRequest);
            if (!response.success()) {
                logger.error("批量获取散标详情失败,listingIds={},error={}", listIds, response.getResultMessage());
                //报警 TODO
                return null;
            }

            List<PPDOpenApiLoanListingDetailBatchResponse.LoanListingDetail> loanListingDetails = response.getLoanListingDetailList();
            if (CollectionUtils.isEmpty(loanListingDetails)) {
                return null;
            }

            //发送详情信息消息 通知策略任务
            handleLoanDetailBatch(loanListingDetails, pageIndex);

            return loanListingDetails;
        }
    }

    /**
     * 批量处理可投的散标详情 目前一次处理10条
     * 一次压缩10个详情 发送MQ
     * 批量写入缓存
     *
     * @param loanListingDetails
     */
    private void handleLoanDetailBatch(List<LoanListingDetail> loanListingDetails, int pageIndex) {
        long start = System.currentTimeMillis();
        //同步发送mq TODO protobuf 序列化 小DO对象 + MQ
        try {
            String message = Base64Util.encodeToString(HessianUtil.toBytes(loanListingDetails));
            LoanableDetailMessage msg = new LoanableDetailMessage(pageIndex, message);
            loanableDetailProducer.sendMsg(msg);
        } catch (Exception e) {
            logger.error("批量处理可投的散标详情失败", e);
        }
        logger.info("单次批量处理可投的散标详情耗时:{}ms,size={}", System.currentTimeMillis() - start, loanListingDetails.size());

    }

    class FilterHisLoanListCallable implements Callable<List<Integer>> {
        private List<Integer> listIds; //投标列表页返回 ListingId

        FilterHisLoanListCallable(List<Integer> listIds) {
            this.listIds = listIds;
        }

        @Override
        public List<Integer> call() {
            //批次查询历史散标列表
            String key = KeyVersionUtils.rediskeyLoanDetailListV(RedisKeyUtils.keyLoanDetailList());
            String[] array = new String[listIds.size()];
            for (int i = 0; i < listIds.size(); i++) {
                array[i] = listIds.get(i).toString();
            }
            List<byte[]> hisLoanListingsSeri = ppdCacheClientHA.Hash().hmgetBit(key, array);

            if (hisLoanListingsSeri == null || hisLoanListingsSeri.size() == 0) {
                //未过滤到
                return listIds;
            }

            //过滤空
            List<Integer> hisList = new ArrayList<>();
            for (byte[] seri : hisLoanListingsSeri) {
                if (seri == null) {
                    continue;
                }
                try {
                    //LoanListingDetail detail = HessianUtil.fromBytes(seri);
                    //Integer id = detail.getListingId();
                    Integer id = Integer.valueOf(new String(seri));
                    hisList.add(id);
                } catch (Exception e) {
                    logger.error("反序列化历史散标详情id失败", e);
                }
            }

            if (hisList == null || hisList.size() == 0) {
                return listIds;
            }

            //过滤掉历史重复散标
            List<Integer> filteredList = new ArrayList<>();
            for (Integer id : listIds) {
                if (!hisList.contains(id)) {
                    filteredList.add(id);
                }
            }
            return filteredList;
        }

    }

}
