package com.zhuobao.ivsyncjob.job.elastic;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.google.common.util.concurrent.RateLimiter;
import com.invest.ivcommons.util.beans.BeanUtils;
import com.invest.ivppad.biz.manager.PPDOpenApiLoanManager;
import com.invest.ivppad.datacache.PPDAccessTokenDataCache;
import com.invest.ivppad.model.PPDUserAccessToken;
import com.invest.ivppad.model.http.request.PPDOpenApiLoanLenderRepaymentRequest;
import com.invest.ivppad.model.http.response.PPDOpenApiLoanLenderRepaymentResponse;
import com.invest.ivuser.biz.manager.LoanRepaymentDetailManager;
import com.invest.ivuser.dao.db.UserLoanRecordDAO;
import com.invest.ivuser.dao.query.UserLoanRecordQuery;
import com.invest.ivuser.datacache.BlackListThirdDataCache;
import com.invest.ivuser.model.entity.BlackListThird;
import com.invest.ivuser.model.entity.LoanRepaymentDetail;
import com.invest.ivuser.model.entity.UserLoanRecord;
import com.zhuobao.ivsyncjob.common.error.ElasticExecuteError;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.invest.ivppad.model.http.response.PPDOpenApiLoanLenderRepaymentResponse.LoanPaymentDetail;

/**
 * TODO MQ
 * 2小时一次 重试
 * Created by xugang on 2017/9/18.
 */
@Component
public class DownloadLenderRepaymentJob extends ElasticBaseJob {

    @Autowired
    protected UserLoanRecordDAO userLoanRecordDAO;

    @Autowired
    protected PPDAccessTokenDataCache ppdAccessTokenDataCache;

    @Autowired
    private LoanRepaymentDetailManager loanRepaymentDetailManager;

    @Autowired
    private PPDOpenApiLoanManager ppdOpenApiLoanManager;

    @Autowired
    private BlackListThirdDataCache blackListThirdDataCache;

    private RateLimiter rateLimiter = RateLimiter.create(2);

    @Override
    public void doExecute(ShardingContext shardingContext) throws ElasticExecuteError {
        int shardingCount = shardingContext.getShardingTotalCount();
        int shardingItem = shardingContext.getShardingItem();

        //获取投资记录 TODO 分页并发处理
        UserLoanRecordQuery query = new UserLoanRecordQuery();
        query.setDownRepaymentFlag(UserLoanRecord.DOWNDETAILFLAG_NO);
        List<UserLoanRecord> userLoanRecordList = userLoanRecordDAO.selectSingleListByQuery(query);
        if (CollectionUtils.isEmpty(userLoanRecordList)) {
            logger.info("获取要处理的投资记录为空");
            return;
        }

        downloadLenderRepayment(userLoanRecordList);
    }

    private void downloadLenderRepayment(List<UserLoanRecord> userLoanRecordList) {

        userLoanRecordList.stream().forEach(o -> {
            Long userId = o.getUserId();
            Integer loanId = o.getLoanId();
            String userName = o.getUsername();
            try {
                //判断是否已在黑名单
                boolean isInBlack = blackListThirdDataCache.isInThirdBlackList(BlackListThird.TYPE_THIRD_USERNAME, userName);
                if (isInBlack) {
                    logger.info("已在黑名单,不去同步还款计划,userName={}", userName);
                    return;
                }

                //获取token
                PPDUserAccessToken ppdUserAccessToken = ppdAccessTokenDataCache.get(userName);
                if (ppdUserAccessToken == null) {
                    logger.error("获取用户投资列表的还款情况失败,未查询到accessToken,username={}", userName);
                    return;
                }
                String accessToken = ppdUserAccessToken.getAccessToken();
                PPDOpenApiLoanLenderRepaymentRequest repaymentRequest = new PPDOpenApiLoanLenderRepaymentRequest();
                repaymentRequest.setOrderId(null);
                repaymentRequest.setListingId(loanId);
                rateLimiter.acquire();
                PPDOpenApiLoanLenderRepaymentResponse response = ppdOpenApiLoanManager.getLenderRepayment(accessToken, repaymentRequest);
                if (!response.success()) {
                    logger.info("获取用户投资列表的还款情况失败,userId={},username={},actoken={},loanId={}",
                            o.getUserId(), o.getUsername(), accessToken, loanId);
                    return;
                }
                if (!response.success()) {
                    logger.info("获取用户投资列表的还款情况失败,userId={},username={},actoken={},loanId={}",
                            o.getUserId(), o.getUsername(), accessToken, loanId);
                    return;
                }

                List<LoanPaymentDetail> loanPaymentDetailList = response.getLoanPaymentDetailList();
                if (CollectionUtils.isEmpty(loanPaymentDetailList)) {
                    logger.info("获取用户投资列表为空,userId={},username={},actoken={},loanId={}",
                            o.getUserId(), o.getUsername(), accessToken, loanId);
                    return;
                }

                //保存还款计划
                List<LoanRepaymentDetail> loanRepaymentDetailList = new ArrayList<>(loanPaymentDetailList.size());
                loanPaymentDetailList.stream().forEach(detail -> {
                    LoanRepaymentDetail loanRepaymentDetail = new LoanRepaymentDetail();
                    BeanUtils.copyProperties(detail, loanRepaymentDetail); //BeanUtils.copyProperties只拷贝不为null的属性
                    loanRepaymentDetail.setUserId(userId);
                    loanRepaymentDetail.setUsername(userName);
                    loanRepaymentDetailList.add(loanRepaymentDetail);
                });
                loanRepaymentDetailManager.saveLoanRepaymentDetail(loanRepaymentDetailList);

                //修改投资列表下载状态
                UserLoanRecord userLoanRecord = new UserLoanRecord();
                userLoanRecord.setId(o.getId());
                userLoanRecord.setDownRepaymentFlag(UserLoanRecord.DOWNDETAILFLAG_YES);
                userLoanRecordDAO.updateByPrimaryKey(userLoanRecord);
            } catch (Exception e) {
                logger.error("处理还款明细异常,loanId=" + loanId + ",userName=" + userName, e);
            }

        });

    }
}
