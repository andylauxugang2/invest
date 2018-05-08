package com.zhuobao.ivsyncjob.job.elastic;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.google.common.util.concurrent.RateLimiter;
import com.invest.ivcommons.util.beans.BeanUtils;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivppad.biz.manager.PPDOpenApiLoanManager;
import com.invest.ivppad.common.PPDLoanRepayStatusEnum;
import com.invest.ivppad.datacache.PPDAccessTokenDataCache;
import com.invest.ivppad.model.PPDUserAccessToken;
import com.invest.ivppad.model.http.request.PPDOpenApiLoanLenderRepaymentRequest;
import com.invest.ivppad.model.http.response.PPDOpenApiLoanLenderRepaymentResponse;
import com.invest.ivuser.dao.db.LoanRepaymentDetailDAO;
import com.invest.ivuser.datacache.BlackListThirdDataCache;
import com.invest.ivuser.model.entity.BlackListThird;
import com.invest.ivuser.model.entity.LoanRepaymentDetail;
import com.zhuobao.ivsyncjob.common.error.ElasticExecuteError;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.invest.ivppad.model.http.response.PPDOpenApiLoanLenderRepaymentResponse.LoanPaymentDetail;

/**
 * Created by xugang on 2017/9/18.
 */
@Component
public abstract class SyncLenderRepaymentBaseJob extends ElasticBaseJob {

    @Autowired
    protected LoanRepaymentDetailDAO loanRepaymentDetailDAO;

    @Autowired
    private PPDOpenApiLoanManager ppdOpenApiLoanManager;

    @Autowired
    protected PPDAccessTokenDataCache ppdAccessTokenDataCache;

    @Autowired
    private BlackListThirdDataCache blackListThirdDataCache;

    private RateLimiter rateLimiter = RateLimiter.create(3.5); //200次/min

    @Override
    public void doExecute(ShardingContext shardingContext) throws ElasticExecuteError {
        List<LoanRepaymentDetail> loanRepaymentDetailList = getLoanRepaymentDetailList();
        syncLenderRepayment(loanRepaymentDetailList, getPPDLoanRepayStatusEnum());
    }

    protected abstract PPDLoanRepayStatusEnum getPPDLoanRepayStatusEnum();

    protected abstract List<LoanRepaymentDetail> getLoanRepaymentDetailList();

    protected void syncLenderRepayment(List<LoanRepaymentDetail> loanRepaymentDetailList, PPDLoanRepayStatusEnum ppdLoanRepayStatusEnum) {

        if (CollectionUtils.isEmpty(loanRepaymentDetailList)) {
            logger.info("获取要同步[" + ppdLoanRepayStatusEnum.getDesc() + "]的还款计划列表为空");
            return;
        }
        if (ppdLoanRepayStatusEnum == null || ppdLoanRepayStatusEnum == PPDLoanRepayStatusEnum.unknown) {
            logger.info("获取要同步[" + ppdLoanRepayStatusEnum.getDesc() + "]参数错误");
            return;
        }

        loanRepaymentDetailList.stream().forEach(o -> {
            Long userId = o.getUserId();
            Integer orderId = o.getOrderId();
            Integer listingId = o.getListingId();
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
                    logger.error("同步还款计划[" + ppdLoanRepayStatusEnum.getDesc() + "]失败,未查询到accessToken,username={}", userName);
                    return;
                }
                String accessToken = ppdUserAccessToken.getAccessToken();

                PPDOpenApiLoanLenderRepaymentRequest repaymentRequest = new PPDOpenApiLoanLenderRepaymentRequest();
                //只看本期还款计划
                repaymentRequest.setOrderId(String.valueOf(orderId));
                repaymentRequest.setListingId(listingId);

                rateLimiter.acquire();
                PPDOpenApiLoanLenderRepaymentResponse response = ppdOpenApiLoanManager.getLenderRepayment(accessToken, repaymentRequest);
                if (!response.success()) {
                    logger.info("获取用户投资列表的还款情况失败,userId={},username={},actoken={},loanId={}",
                            o.getUserId(), o.getUsername(), accessToken, listingId);
                    return;
                }
                if (!response.success()) {
                    logger.info("获取用户投资列表的还款情况失败,userId={},username={},actoken={},loanId={}",
                            o.getUserId(), o.getUsername(), accessToken, listingId);
                    return;
                }

                List<LoanPaymentDetail> loanPaymentDetailList = response.getLoanPaymentDetailList();
                if (CollectionUtils.isEmpty(loanPaymentDetailList)) {
                    logger.info("获取用户投资列表为空,userId={},username={},actoken={},loanId={}",
                            o.getUserId(), o.getUsername(), accessToken, listingId);
                    return;
                }

                //同步还款计划 2->1 2->2 2->3
                LoanPaymentDetail detail = loanPaymentDetailList.get(0);
                if (detail.getOrderId() != orderId) {
                    logger.error("返回的还款计划order匹配错误,orderId1=" + orderId + ",orderId2=" + detail.getOrderId());
                    return;
                }

                BeanUtils.copyProperties(detail, o); //BeanUtils.copyProperties只拷贝不为null的属性
                o.setUpdateTime(DateUtil.getCurrentDatetime());

                //更新还款计划
                loanRepaymentDetailDAO.updateByPrimaryKey(o);
            } catch (Exception e) {
                logger.error("同步[" + ppdLoanRepayStatusEnum.getDesc() + "]还款明细异常,listingId=" + listingId + ",userName=" + userName + ",msg=" + e.getMessage());
                //403等http异常处理 放入重试表 TODO
            }
        });

    }
}
