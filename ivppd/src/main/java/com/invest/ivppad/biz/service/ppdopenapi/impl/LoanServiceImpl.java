package com.invest.ivppad.biz.service.ppdopenapi.impl;

import com.invest.ivcommons.constant.SystemErrorEnum;
import com.invest.ivppad.biz.manager.PPDOpenApiLoanListManager;
import com.invest.ivppad.biz.manager.PPDOpenApiLoanManager;
import com.invest.ivppad.biz.service.ppdopenapi.LoanService;
import com.invest.ivppad.common.IVPPDErrorEnum;
import com.invest.ivppad.datacache.PPDAccessTokenDataCache;
import com.invest.ivppad.model.PPDUserAccessToken;
import com.invest.ivppad.model.http.request.*;
import com.invest.ivppad.model.http.response.*;
import com.invest.ivppad.model.param.*;
import com.invest.ivppad.model.result.PPDOpenApiLoanResult;
import com.invest.ivuser.model.entity.UserLoanRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xugang on 2017/8/1.
 */
@Service
public class LoanServiceImpl implements LoanService {
    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);

    @Autowired
    private PPDOpenApiLoanManager loanManager;
    @Autowired
    private PPDOpenApiLoanListManager loanListManager;

    @Autowired
    protected PPDAccessTokenDataCache ppdAccessTokenDataCache;

    @Override
    public PPDOpenApiLoanResult getLoanList(PPDOpenApiGetLoanListParam param) {
        PPDOpenApiLoanResult result = new PPDOpenApiLoanResult();
        String userName = param.getUserName();
        try {
            //获取散标可投标列表
            PPDOpenApiLoanListRequest apiLoanListRequest = new PPDOpenApiLoanListRequest();
            apiLoanListRequest.setPageIndex(param.getPageIndex());
            if (param.getStartDateTime() != null)
                apiLoanListRequest.setStartDateTime(param.getStartDateTime().toString());

            PPDOpenApiLoanListResponse response = loanListManager.getLoanList(apiLoanListRequest);
            if (!response.success()) {
                SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
                result.setErrorMsg(response.getResultMessage());
                logger.error("获取散标可投标列表失败,userName={},error={}", userName, response.getResultMessage());
                return result;
            }

            List<PPDOpenApiLoanListResponse.LoanInfo> loanInfoList = response.getLoanInfoList();
            result.setLoanInfoList(loanInfoList);
        } catch (Exception e) {
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            logger.error("获取散标可投标列表失败,userName=" + userName, e);
        }

        return result;
    }

    @Override
    public PPDOpenApiLoanResult getLoanListingDetailBatch(PPDOpenApiGetLoanListingDetailBatchParam param) {
        PPDOpenApiLoanResult result = new PPDOpenApiLoanResult();
        String userName = param.getUserName();
        try {
            //获取散标可投标列表
            PPDOpenApiLoanListingDetailBatchRequest loanListingDetailBatchRequest = new PPDOpenApiLoanListingDetailBatchRequest();
            loanListingDetailBatchRequest.setListIds(param.getListingIds());

            PPDOpenApiLoanListingDetailBatchResponse response = loanListManager.getListingDetailBatch(loanListingDetailBatchRequest);
            if (!response.success()) {
                SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
                result.setErrorMsg(response.getResultMessage());
                logger.error("批量获取散标详情失败,userName={},listingIds={},error={}", userName, param.getListingIds(), response.getResultMessage());
                return result;
            }

            List<PPDOpenApiLoanListingDetailBatchResponse.LoanListingDetail> loanListingDetailList = response.getLoanListingDetailList();
            result.setLoanListingDetailList(loanListingDetailList);
        } catch (Exception e) {
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            logger.error("批量获取散标详情失败,userName=" + userName + ",listingIds=" + param.getListingIds(), e);
        }

        return result;
    }

    @Override
    public PPDOpenApiLoanResult getLoanListingStatusBatch(PPDOpenApiGetLoanListingStatusBatchParam param) {
        PPDOpenApiLoanResult result = new PPDOpenApiLoanResult();
        String userName = param.getUserName();
        try {
            //批量查询散标投资状态
            PPDOpenApiLoanListingStatusBatchRequest loanListingStatusBatchRequest = new PPDOpenApiLoanListingStatusBatchRequest();
            loanListingStatusBatchRequest.setListIds(param.getListingIds());

            PPDOpenApiLoanListingStatusBatchResponse response = loanManager.getListingStatusBatch(loanListingStatusBatchRequest);
            if (!response.success()) {
                SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
                result.setErrorMsg(response.getResultMessage());
                logger.error("批量查询散标投资状态失败,userName={},listingIds={},error={}", userName, param.getListingIds(), response.getResultMessage());
                return result;
            }

            List<PPDOpenApiLoanListingStatusBatchResponse.LoanListingStatus> loanListingStatusList = response.getLoanListingStatusList();
            result.setLoanListingStatusList(loanListingStatusList);
        } catch (Exception e) {
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            logger.error("批量查询散标投资状态失败,userName=" + userName + ",listingIds=" + param.getListingIds(), e);
        }

        return result;
    }

    @Override
    public PPDOpenApiLoanResult getUserLoanBidList(PPDOpenApiGetUserLoanBidListParam param) {
        PPDOpenApiLoanResult result = new PPDOpenApiLoanResult();
        String userName = param.getUserName();
        try {
            //获取用户投标记录
            PPDOpenApiLoanUserBidListRequest request = new PPDOpenApiLoanUserBidListRequest();
            request.setListingId(param.getListingId());
            request.setStartTime(param.getStartTime());
            request.setEndTime(param.getEndTime());
            request.setPageIndex(param.getPageIndex());
            request.setPageSize(param.getPageSize());

            PPDOpenApiLoanUserBidListResponse response = loanManager.getUserBidList(userName, request);
            if (!response.success()) {
                SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
                result.setErrorMsg(response.getResultMessage());
                logger.error("获取用户投标记录失败,userName={},param={},error={}", userName, param, response.getResultMessage());
                return result;
            }

            result.setUserLoanBidList(response);
        } catch (Exception e) {
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            logger.error("获取用户投标记录失败,userName=" + userName + ",param=" + param, e);
        }

        return result;
    }

    @Override
    public PPDOpenApiLoanResult getLoanListingBidRecordList(PPDOpenApiGetLoanListingBidRecordBatchParam param) {
        PPDOpenApiLoanResult result = new PPDOpenApiLoanResult();
        String userName = param.getUserName();
        try {
            //获取用户投标记录
            PPDOpenApiLoanListingBidRecordBatchRequest request = new PPDOpenApiLoanListingBidRecordBatchRequest();
            request.setListIds(param.getListingIds());

            PPDOpenApiLoanListingBidRecordBatchResponse response = loanManager.getLoanListingBidList(request);
            if (!response.success()) {
                SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
                result.setErrorMsg(response.getResultMessage());
                logger.error("获取用户投标记录失败,userName={},listingIds={},error={}", userName, param.getListingIds(), response.getResultMessage());
                return result;
            }

            List<PPDOpenApiLoanListingBidRecordBatchResponse.ListingBidDetail> loanListingBidlList = response.getLoanListingBidDetailList();
            result.setLoanListingBidRecordList(loanListingBidlList);
        } catch (Exception e) {
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            logger.error("获取用户投标记录失败,userName=" + userName + ",listingIds=" + param.getListingIds(), e);
        }

        return result;
    }

    @Override
    public PPDOpenApiLoanResult getUserLoanRepaymentDetail(PPDOpenApiGetUserLoanRepaymentParam param) {
        PPDOpenApiLoanResult result = new PPDOpenApiLoanResult();
        String userName = param.getUserName();

        try {
            //获取token
            PPDUserAccessToken ppdUserAccessToken = ppdAccessTokenDataCache.get(userName);
            if (ppdUserAccessToken == null) {
                IVPPDErrorEnum.CAN_NOT_GET_ACCESS_TOKEN_ERROR.fillResult(result);
                logger.error("未查询到accessToken,username={}", userName);
                return result;
            }
            String token = ppdUserAccessToken.getAccessToken();

            //获取用户投资列表的还款情况
            PPDOpenApiLoanLenderRepaymentRequest request = new PPDOpenApiLoanLenderRepaymentRequest();
            request.setListingId(param.getListingId());
            if (StringUtils.isNotEmpty(param.getPeriods())) request.setOrderId(param.getPeriods());
            PPDOpenApiLoanLenderRepaymentResponse response = loanManager.getLenderRepayment(token, request);

            if (!response.success()) {
                SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
                result.setErrorMsg(response.getResultMessage());
                logger.error("获取用户投资列表的还款情况失败,userName={},listingId={},periods={},error={}", new Object[]{userName, param.getListingId(), param.getPeriods(), response.getResultMessage()});
                return result;
            }

            List<PPDOpenApiLoanLenderRepaymentResponse.LoanPaymentDetail> loanPaymentDetailList = response.getLoanPaymentDetailList();
            result.setUserLoanPaymentDetailList(loanPaymentDetailList);
        } catch (Exception e) {
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            logger.error("获取用户投资列表的还款情况失败,userName=" + userName + ",listingId=" + param.getListingId() + "periods=" + param.getPeriods(), e);
        }

        return result;
    }

    @Override
    public PPDOpenApiLoanResult bidLoan(PPDOpenApiBidLoanParam param) {
        long start = System.currentTimeMillis();
        PPDOpenApiLoanResult result = new PPDOpenApiLoanResult();
        String userName = param.getUserName();
        int listingId = param.getListingId();
        try {
            //获取token
            PPDUserAccessToken ppdUserAccessToken = ppdAccessTokenDataCache.get(userName);
            if (ppdUserAccessToken == null) {
                IVPPDErrorEnum.CAN_NOT_GET_ACCESS_TOKEN_ERROR.fillResult(result);
                logger.error("未查询到accessToken,username={}", userName);
                return result;
            }
            String token = ppdUserAccessToken.getAccessToken();

            //投标
            PPDOpenApiBidLoanRequest request = new PPDOpenApiBidLoanRequest();
            request.setListingId(listingId);
            request.setAmount(param.getAmount());
            request.setUseCoupon(param.getUseCoupon());
            PPDOpenApiBidLoanResponse response = loanManager.bidLoan(token, request);

            if (!response.success()) {
                SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
                result.setErrorMsg(response.getResultMessage());
                logger.error("投标失败,userName={},listingId={},amount={},extparam={},error={}", new Object[]{userName, param.getListingId(), param.getAmount(), param.getExtParam(), response.getResultMessage()});
                return result;
            }

            UserLoanRecord userLoanRecord = buildUserLoanRecord(userName, response);
            result.setUserLoanRecord(userLoanRecord);
            logger.info("投标成功,userId={},username={},listingId={},amount={},extparam={},耗时={}",
                    new Object[]{param.getUserId(), userName, listingId, param.getAmount(), param.getExtParam(), System.currentTimeMillis() - start});
            ;
        } catch (Exception e) {
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            logger.error("投标失败,userName=" + userName + ",listingId=" + param.getListingId() + ",extparam=" + param.getExtParam(), e);
        }

        return result;
    }

    @Override
    public PPDOpenApiLoanResult bidLoan(PPDOpenApiBidLoanParam param, FutureCallback<HttpResponse> futureCallback) {
        PPDOpenApiLoanResult result = new PPDOpenApiLoanResult();
        String userName = param.getUserName();
        int listingId = param.getListingId();
        try {
            //获取token
            PPDUserAccessToken ppdUserAccessToken = ppdAccessTokenDataCache.get(userName);
            if (ppdUserAccessToken == null) {
                IVPPDErrorEnum.CAN_NOT_GET_ACCESS_TOKEN_ERROR.fillResult(result);
                logger.error("未查询到accessToken,username={}", userName);
                return result;
            }
            String token = ppdUserAccessToken.getAccessToken();

            //投标
            PPDOpenApiBidLoanRequest request = new PPDOpenApiBidLoanRequest();
            request.setListingId(listingId);
            request.setAmount(param.getAmount());
            request.setUseCoupon(param.getUseCoupon());
            loanManager.invokeBidLoan(token, request, futureCallback);
        } catch (Exception e) {
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            logger.error("通知selector投标异常,userName=" + userName + ",listingId=" + param.getListingId() + ",extparam=" + param.getExtParam(), e);
        }

        return result;
    }

    public static UserLoanRecord buildUserLoanRecord(String username, PPDOpenApiBidLoanResponse response) {
        UserLoanRecord record = new UserLoanRecord();
        record.setUsername(username);
        record.setLoanId(response.getListingId());
        record.setAmount(response.getAmount());
        record.setCouponAmount(response.getCouponAmount());
        record.setParticipationAmount(response.getParticipationAmount());
        record.setCouponStatus((byte) response.getCouponStatus());
        return record;
    }


    /**
     * 根据用户名查询token
     *
     * @param userName
     * @return
     */
    public PPDUserAccessToken getUserNameAccessToken(String userName) {
        return ppdAccessTokenDataCache.get(userName);
    }
}
