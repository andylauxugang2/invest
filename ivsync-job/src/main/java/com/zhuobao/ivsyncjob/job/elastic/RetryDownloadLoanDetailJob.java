package com.zhuobao.ivsyncjob.job.elastic;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.google.common.util.concurrent.RateLimiter;
import com.invest.ivcommons.util.format.DateFormatUtil;
import com.invest.ivppad.biz.service.ppdopenapi.LoanService;
import com.invest.ivppad.model.param.PPDOpenApiGetLoanListingDetailBatchParam;
import com.invest.ivppad.model.result.PPDOpenApiLoanResult;
import com.invest.ivuser.biz.manager.LoanDetailManager;
import com.invest.ivuser.dao.db.UserLoanRecordDAO;
import com.invest.ivuser.dao.query.UserLoanRecordQuery;
import com.invest.ivuser.model.entity.LoanDetail;
import com.invest.ivuser.model.entity.UserLoanRecord;
import com.zhuobao.ivsyncjob.common.error.ElasticExecuteError;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.invest.ivppad.model.http.response.PPDOpenApiLoanListingDetailBatchResponse.LoanListingDetail;

/**
 * MQ 消费失败重试
 * 2小时一次 重试
 * Created by xugang on 2017/9/18.
 */
@Component
public class RetryDownloadLoanDetailJob extends ElasticBaseJob {

    @Autowired
    protected UserLoanRecordDAO userLoanRecordDAO;

    @Resource
    private LoanService loanService;

    @Autowired
    private LoanDetailManager loanDetailManager;

    private RateLimiter rateLimiter = RateLimiter.create(2.0); //接口限制500次/min 此处不要设置太大,会影响正常投标

    @Override
    public void doExecute(ShardingContext shardingContext) throws ElasticExecuteError {
        int shardingCount = shardingContext.getShardingTotalCount();
        int shardingItem = shardingContext.getShardingItem();

        //获取本月投资记录 TODO 分页并发处理
        UserLoanRecordQuery query = new UserLoanRecordQuery();
        query.setDownDetailFlag(UserLoanRecord.DOWNDETAILFLAG_NO);
        List<Integer> loanIds = userLoanRecordDAO.selectGroupLoanIdByQuery(query);
        if (CollectionUtils.isEmpty(loanIds)) {
            logger.info("获取要处理的投资记录为空");
            return;
        }

        downloadLoanDetailBatch(loanIds);
    }

    private void downloadLoanDetailBatch(List<Integer> loanIds) {
        int batchSize = PPDOpenApiGetLoanListingDetailBatchParam.FETCH_BATCH_DETAIL_SIZE;
        List<Integer> listingIdsTemp = new ArrayList<>(batchSize);
        int counter = 0;
        for (Integer loanId : loanIds) {
            if (loanId == null) {
                continue;
            }
            listingIdsTemp.add(loanId);
            counter++;
            //存够batchSize条发给下游线程处理
            if (counter % batchSize == 0) {
                rateLimiter.acquire();
                downloadLoanDetail(listingIdsTemp);
                listingIdsTemp = new ArrayList<>(batchSize);
            }

        }
        if (listingIdsTemp.size() > 0) {
            rateLimiter.acquire();
            downloadLoanDetail(listingIdsTemp);
        }
    }

    private void downloadLoanDetail(List<Integer> listIds) {
        PPDOpenApiGetLoanListingDetailBatchParam param = new PPDOpenApiGetLoanListingDetailBatchParam();
        param.setListingIds(listIds);
        //批量下载标的详情信息 一次接口返回10条数据 单次下载耗时为5000毫秒左右,多线程下载平均单次查询耗时为350毫秒
        PPDOpenApiLoanResult result = loanService.getLoanListingDetailBatch(param);
        if (result.isFailed()) {
            logger.error("获取散标详情信息失败,error={}", result);
            return;
        }
        List<LoanListingDetail> loanListingDetails = result.getLoanListingDetailList();

        if (CollectionUtils.isEmpty(loanListingDetails)) {
            logger.error("获取散标详情为空,listIds=" + listIds);
            return;
        }

        loanListingDetails.stream().forEach(o -> handleListingDetail(o));
    }

    private void handleListingDetail(LoanListingDetail loanListingDetail) {
        try {
            LoanDetail loanDetail = new LoanDetail();
            //BeanUtils.copyProperties只拷贝不为null的属性
            //BeanUtils.copyProperties(loanListingDetail, loanDetail);
            loanDetail.setListingId(loanListingDetail.getListingId());
            loanDetail.setAmount(loanListingDetail.getAmount());
            loanDetail.setMonths(loanListingDetail.getMonths());
            loanDetail.setRate(loanListingDetail.getCurrentRate());
            loanDetail.setLenderCount(loanListingDetail.getLenderCount());
            loanDetail.setCreditCode(loanListingDetail.getCreditCode());
            loanDetail.setBorrowName(loanListingDetail.getBorrowName());
            loanDetail.setGender(loanListingDetail.getGender());
            loanDetail.setAge(loanListingDetail.getAge());
            loanDetail.setEducationDegree(loanListingDetail.getEducationDegree());
            loanDetail.setGraduateSchool(loanListingDetail.getGraduateSchool());
            loanDetail.setStudyStyle(loanListingDetail.getStudyStyle());
            loanDetail.setSuccessCount(loanListingDetail.getSuccessCount());
            loanDetail.setWasteCount(loanListingDetail.getWasteCount());
            loanDetail.setCancelCount(loanListingDetail.getCancelCount());
            loanDetail.setFailedCount(loanListingDetail.getFailedCount());
            loanDetail.setNormalCount(loanListingDetail.getNormalCount());
            loanDetail.setOverdueLessCount(loanListingDetail.getOverdueLessCount());
            loanDetail.setOverdueMoreCount(loanListingDetail.getOverdueMoreCount());
            loanDetail.setOwingAmount(loanListingDetail.getOwingAmount());
            loanDetail.setOwingPrincipal(loanListingDetail.getOwingPrincipal());
            loanDetail.setHighestDebt(loanListingDetail.getHighestDebt());
            loanDetail.setHighestPrincipal(loanListingDetail.getHighestPrincipal());
            loanDetail.setTotalPrincipal(loanListingDetail.getTotalPrincipal());
            loanDetail.setAmountToReceive(loanListingDetail.getAmountToReceive());
            loanDetail.setThirdAuthFlag(loanListingDetail.getThirdAuthFlag());

            loanDetail.setFistBidTime(DateFormatUtil.convertStr2Date(loanListingDetail.getFistBidTime(), DateFormatUtil.PATTERN_UTC_NO_ZONE_WITHMILLIS));
            loanDetail.setLastBidTime(DateFormatUtil.convertStr2Date(loanListingDetail.getLastBidTime(), DateFormatUtil.PATTERN_UTC_NO_ZONE_WITHMILLIS));
            loanDetail.setFirstSuccessBorrowTime(DateFormatUtil.convertStr2Date(loanListingDetail.getFirstSuccessBorrowTime(), DateFormatUtil.PATTERN_UTC_NO_ZONE_WITHSECOND));
            loanDetail.setLastSuccessBorrowTime(DateFormatUtil.convertStr2Date(loanListingDetail.getLastSuccessBorrowTime(), DateFormatUtil.PATTERN_UTC_NO_ZONE_WITHSECOND));
            loanDetail.setRegisterTime(DateFormatUtil.convertStr2Date(loanListingDetail.getRegisterTime(), DateFormatUtil.PATTERN_UTC_NO_ZONE_WITHSECOND));
            loanDetailManager.saveLoanDetail(loanDetail);

            //修改投资列表下载状态
            UserLoanRecord userLoanRecord = new UserLoanRecord();
            userLoanRecord.setLoanId(loanDetail.getListingId());
            userLoanRecord.setDownDetailFlag(UserLoanRecord.DOWNDETAILFLAG_YES);
            userLoanRecordDAO.updateListByLoanId(userLoanRecord);
        } catch (Exception e) {
            logger.error("处理标的详情发生异常,listingId=" + loanListingDetail.getListingId(), e);
        }
    }
}
