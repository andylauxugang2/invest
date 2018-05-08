package com.invest.ivppad.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivppad.base.property.PropertyObject;
import com.invest.ivppad.base.property.ValueTypeEnum;
import com.invest.ivppad.common.exception.PPDOpenApiInvokeException;
import com.invest.ivppad.datacache.PPDAccessTokenDataCache;
import com.invest.ivppad.model.PPDUserAccessToken;
import com.invest.ivppad.model.http.request.*;
import com.invest.ivppad.model.http.response.*;
import com.invest.ivppad.util.HttpAsyncClientBidLoanUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;

/**
 * 标的 底层工具
 * Created by xugang on 2017/8/1.
 */
@Component
public class PPDOpenApiLoanManager extends PPDOpenApiManagerBase {
    private static final Logger logger = LoggerFactory.getLogger(PPDOpenApiLoanManager.class);

    //获取散标可投标列表 url
    @Value(value = "${openapi.invest.loan.loanListUrl}")
    private String loanListUrl;
    //获取散标可投标列表 url
    @Value(value = "${openapi.invest.loan.batchListingInfosUrl}")
    private String batchListingInfosUrl;
    //批量查询散标投资状态 url
    @Value(value = "${openapi.invest.loan.batchListingStatusInfosUrl}")
    private String batchListingStatusInfosUrl;
    //批量查询散标投资状态 url
    @Value(value = "${openapi.invest.loan.userBidListUrl}")
    private String userBidListUrl;
    //批量获取散标投资记录 url
    @Value(value = "${openapi.invest.loan.batchListingBidListUrl}")
    private String batchListingBidListUrl;
    //获取用户投资列表的还款情况 url
    @Value(value = "${openapi.invest.loan.fetchLenderRepaymentUrl}")
    private String fetchLenderRepaymentUrl;
    //投标 url
    @Value(value = "${openapi.invest.loan.bidUrl}")
    private String bidUrl;

    @Autowired
    protected PPDAccessTokenDataCache ppdAccessTokenDataCache;

    //单次批量可查20条数据
    public PPDOpenApiLoanListingStatusBatchResponse getListingStatusBatch(PPDOpenApiLoanListingStatusBatchRequest request) {
        StopWatch stopWatch = new StopWatch("批量查询散标投资状态耗时统计");

        try {
            HttpHeaders headers = getRequestHeadersCommon(null, new PropertyObject(PPDOpenApiLoanListingStatusBatchRequest.PARAM_NAME_LISTINGIDS, request.getListIds(), ValueTypeEnum.Other));
            ParameterizedTypeReference responseType = new ParameterizedTypeReference<PPDOpenApiLoanListingStatusBatchResponse>() {
            };
            stopWatch.start("批量查询散标投资状态");
            JSONObject jsonCredentials = new JSONObject();
            jsonCredentials.put(PPDOpenApiLoanListingStatusBatchRequest.PARAM_NAME_LISTINGIDS, request.getListIds());
            ResponseEntity<PPDOpenApiLoanListingStatusBatchResponse> responseEntity = callRetry(batchListingStatusInfosUrl, jsonCredentials.toString(), HttpMethod.POST, headers, responseType);
            stopWatch.stop();

            int statusCode = responseEntity.getStatusCode().value();
            if (statusCode != HttpStatus.OK.value()) {
                throw new IllegalStateException("批量查询散标投资状态接口[" + batchListingStatusInfosUrl + "]状态返回非法 statusCode=" + statusCode);
            }

            PPDOpenApiLoanListingStatusBatchResponse response = responseEntity.getBody();
            return response;
        } catch (Exception e) {
            logger.error("批量查询散标投资状态接口发生异常", e);
            throw new PPDOpenApiInvokeException(e.getMessage());
        } finally {
            logger.info("[manger]{}", stopWatch.prettyPrint());
        }
    }

    /**
     * 注意:1.刚投完的表,不能马上查出投标记录
     *
     * @param userName
     * @param request
     * @return
     */
    public PPDOpenApiLoanUserBidListResponse getUserBidList(String userName, PPDOpenApiLoanUserBidListRequest request) {
        StopWatch stopWatch = new StopWatch("获取用户投标记录耗时统计");

        //获取token
        PPDUserAccessToken ppdUserAccessToken = ppdAccessTokenDataCache.get(userName);
        if (ppdUserAccessToken == null) {
            throw new IllegalStateException("查询用户token为空");
        }
        String token = ppdUserAccessToken.getAccessToken();

        try {
            HttpHeaders headers = getRequestHeadersCommon(token,
                    new PropertyObject(PPDOpenApiLoanUserBidListRequest.PARAM_NAME_LISTINGID, request.getListingId(), ValueTypeEnum.Int32),
                    new PropertyObject(PPDOpenApiLoanUserBidListRequest.PARAM_NAME_STARTTIME, request.getStartTime(), ValueTypeEnum.DateTime),
                    new PropertyObject(PPDOpenApiLoanUserBidListRequest.PARAM_NAME_ENDTIME, request.getEndTime(), ValueTypeEnum.DateTime),
                    new PropertyObject(PPDOpenApiLoanUserBidListRequest.PARAM_NAME_PAGEINDEX, request.getPageIndex(), ValueTypeEnum.Int32),
                    new PropertyObject(PPDOpenApiLoanUserBidListRequest.PARAM_NAME_PAGESIZE, request.getPageSize(), ValueTypeEnum.Int32)
            );
            ParameterizedTypeReference responseType = new ParameterizedTypeReference<PPDOpenApiLoanUserBidListResponse>() {
            };
            stopWatch.start("获取用户投标记录");
            JSONObject jsonCredentials = new JSONObject();
            jsonCredentials.put(PPDOpenApiLoanUserBidListRequest.PARAM_NAME_LISTINGID, request.getListingId());
            jsonCredentials.put(PPDOpenApiLoanUserBidListRequest.PARAM_NAME_STARTTIME, request.getStartTime());
            jsonCredentials.put(PPDOpenApiLoanUserBidListRequest.PARAM_NAME_ENDTIME, request.getEndTime());
            jsonCredentials.put(PPDOpenApiLoanUserBidListRequest.PARAM_NAME_PAGEINDEX, request.getPageIndex());
            jsonCredentials.put(PPDOpenApiLoanUserBidListRequest.PARAM_NAME_PAGESIZE, request.getPageSize());
            ResponseEntity<PPDOpenApiLoanUserBidListResponse> responseEntity = callRetry(userBidListUrl, jsonCredentials.toString(), HttpMethod.POST, headers, responseType);
            stopWatch.stop();

            int statusCode = responseEntity.getStatusCode().value();
            if (statusCode != HttpStatus.OK.value()) {
                throw new IllegalStateException("获取用户投标记录接口[" + userBidListUrl + "]状态返回非法 statusCode=" + statusCode);
            }

            PPDOpenApiLoanUserBidListResponse response = responseEntity.getBody();
            return response;
        } catch (Exception e) {
            logger.error("获取用户投标记录接口发生异常", e);
            throw new PPDOpenApiInvokeException(e.getMessage());
        } finally {
            logger.info("[manger]{}", stopWatch.prettyPrint());
        }
    }

    public PPDOpenApiLoanListingBidRecordBatchResponse getLoanListingBidList(PPDOpenApiLoanListingBidRecordBatchRequest request) {
        StopWatch stopWatch = new StopWatch("批量获取散标投资记录耗时统计");

        try {
            HttpHeaders headers = getRequestHeadersCommon(null, new PropertyObject(PPDOpenApiLoanListingBidRecordBatchRequest.PARAM_NAME_LISTINGIDS, request.getListIds(), ValueTypeEnum.Other));
            ParameterizedTypeReference responseType = new ParameterizedTypeReference<PPDOpenApiLoanListingBidRecordBatchResponse>() {
            };
            stopWatch.start("批量获取散标投资记录");
            JSONObject jsonCredentials = new JSONObject();
            jsonCredentials.put(PPDOpenApiLoanListingBidRecordBatchRequest.PARAM_NAME_LISTINGIDS, request.getListIds());
            ResponseEntity<PPDOpenApiLoanListingBidRecordBatchResponse> responseEntity = callRetry(batchListingBidListUrl, jsonCredentials.toString(), HttpMethod.POST, headers, responseType);
            stopWatch.stop();

            int statusCode = responseEntity.getStatusCode().value();
            if (statusCode != HttpStatus.OK.value()) {
                throw new IllegalStateException("批量获取散标投资记录接口[" + batchListingBidListUrl + "]状态返回非法 statusCode=" + statusCode);
            }

            PPDOpenApiLoanListingBidRecordBatchResponse response = responseEntity.getBody();
            return response;
        } catch (Exception e) {
            logger.error("批量获取散标投资记录接口发生异常", e);
            throw new PPDOpenApiInvokeException(e.getMessage());
        } finally {
            logger.info("[manger]{}", stopWatch.prettyPrint());
        }
    }

    //一个用户投资的标,其还款情况,逾期情况查看
    public PPDOpenApiLoanLenderRepaymentResponse getLenderRepayment(String accessToken, PPDOpenApiLoanLenderRepaymentRequest request) {
        long start = System.currentTimeMillis();
        try {
            HttpHeaders headers = getRequestHeadersCommon(accessToken,
                    new PropertyObject(PPDOpenApiLoanLenderRepaymentRequest.PARAM_NAME_LISTINGID, request.getListingId(), ValueTypeEnum.Int32),
                    new PropertyObject(PPDOpenApiLoanLenderRepaymentRequest.PARAM_NAME_ORDERID, request.getOrderId(), ValueTypeEnum.String)
            );
            ParameterizedTypeReference responseType = new ParameterizedTypeReference<PPDOpenApiLoanLenderRepaymentResponse>() {
            };
            JSONObject jsonCredentials = new JSONObject();
            jsonCredentials.put(PPDOpenApiLoanLenderRepaymentRequest.PARAM_NAME_LISTINGID, request.getListingId());
            if (StringUtils.isNotEmpty(request.getOrderId()))
                jsonCredentials.put(PPDOpenApiLoanLenderRepaymentRequest.PARAM_NAME_ORDERID, request.getOrderId());
            ResponseEntity<PPDOpenApiLoanLenderRepaymentResponse> responseEntity = callRetry(fetchLenderRepaymentUrl, jsonCredentials.toString(), HttpMethod.POST, headers, responseType);

            int statusCode = responseEntity.getStatusCode().value();
            if (statusCode != HttpStatus.OK.value()) {
                throw new IllegalStateException("获取用户投资列表的还款情况接口[" + fetchLenderRepaymentUrl + "]状态返回非法 statusCode=" + statusCode);
            }

            PPDOpenApiLoanLenderRepaymentResponse response = responseEntity.getBody();
            return response;
        } catch (Exception e) {
            logger.error("获取用户投资列表的还款情况接口发生异常,msg=" + e.getMessage());
            throw new PPDOpenApiInvokeException(e.getMessage());
        } finally {
            logger.info("获取用户投资列表的还款情况耗时:{}", System.currentTimeMillis() - start);
        }
    }

    public PPDOpenApiBidLoanResponse bidLoan(String accessToken, PPDOpenApiBidLoanRequest request) {
        try {
            HttpHeaders headers = getRequestHeadersCommon(accessToken,
                    new PropertyObject(PPDOpenApiBidLoanRequest.PARAM_NAME_LISTINGID, request.getListingId(), ValueTypeEnum.Int32),
                    new PropertyObject(PPDOpenApiBidLoanRequest.PARAM_NAME_AMOUNT, request.getAmount(), ValueTypeEnum.Double),
                    new PropertyObject(PPDOpenApiBidLoanRequest.PARAM_NAME_USECOUPON, request.getUseCoupon(), ValueTypeEnum.String)
            );
            ParameterizedTypeReference responseType = new ParameterizedTypeReference<PPDOpenApiBidLoanResponse>() {
            };
            JSONObject jsonCredentials = new JSONObject();
            jsonCredentials.put(PPDOpenApiBidLoanRequest.PARAM_NAME_LISTINGID, request.getListingId());
            jsonCredentials.put(PPDOpenApiBidLoanRequest.PARAM_NAME_AMOUNT, request.getAmount());
            if (StringUtils.isNotEmpty(request.getUseCoupon()))
                jsonCredentials.put(PPDOpenApiBidLoanRequest.PARAM_NAME_USECOUPON, request.getUseCoupon());
            ResponseEntity<PPDOpenApiBidLoanResponse> responseEntity = callRetry(bidUrl, jsonCredentials.toString(), HttpMethod.POST, headers, responseType);

            int statusCode = responseEntity.getStatusCode().value();
            if (statusCode != HttpStatus.OK.value()) {
                throw new IllegalStateException("投标接口[" + bidUrl + "]状态返回非法 statusCode=" + statusCode);
            }

            PPDOpenApiBidLoanResponse response = responseEntity.getBody();
            return response;
        } catch (Exception e) {
            logger.error("投标接口发生异常", e);
            throw new PPDOpenApiInvokeException(e.getMessage());
        }
    }

    public void invokeBidLoan(String accessToken, PPDOpenApiBidLoanRequest request, FutureCallback<HttpResponse> futureCallback) {
        try {
            Map<String, String> headers = getRequestCommonHeaders(accessToken,
                    new PropertyObject(PPDOpenApiBidLoanRequest.PARAM_NAME_LISTINGID, request.getListingId(), ValueTypeEnum.Int32),
                    new PropertyObject(PPDOpenApiBidLoanRequest.PARAM_NAME_AMOUNT, request.getAmount(), ValueTypeEnum.Double),
                    new PropertyObject(PPDOpenApiBidLoanRequest.PARAM_NAME_USECOUPON, request.getUseCoupon(), ValueTypeEnum.String));
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PPDOpenApiBidLoanRequest.PARAM_NAME_LISTINGID, String.valueOf(request.getListingId()));
            paramMap.put(PPDOpenApiBidLoanRequest.PARAM_NAME_AMOUNT, String.valueOf(request.getAmount()));
            paramMap.put(PPDOpenApiBidLoanRequest.PARAM_NAME_USECOUPON, request.getUseCoupon());
            HttpAsyncClientBidLoanUtil.post(bidUrl, paramMap, headers, futureCallback);
        } catch (Exception e) {
            logger.error("调用散标可投标列表接口发生异常", e);
            throw new PPDOpenApiInvokeException(e.getMessage());
        }
    }
}
