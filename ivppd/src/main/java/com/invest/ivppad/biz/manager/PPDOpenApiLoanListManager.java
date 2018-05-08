package com.invest.ivppad.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivppad.base.property.PropertyObject;
import com.invest.ivppad.base.property.ValueTypeEnum;
import com.invest.ivppad.common.exception.PPDOpenApiInvokeException;
import com.invest.ivppad.model.http.request.PPDOpenApiLoanListRequest;
import com.invest.ivppad.model.http.request.PPDOpenApiLoanListingDetailBatchRequest;
import com.invest.ivppad.model.http.response.PPDOpenApiLoanListResponse;
import com.invest.ivppad.model.http.response.PPDOpenApiLoanListingDetailBatchResponse;
import com.invest.ivppad.util.HttpAsyncClientUtil;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 标的 底层工具
 * Created by xugang on 2017/8/1.
 */
@Component
public class PPDOpenApiLoanListManager extends PPDOpenApiManagerBase {
    private static final Logger logger = LoggerFactory.getLogger(PPDOpenApiLoanListManager.class);

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

    public PPDOpenApiLoanListResponse getLoanList(PPDOpenApiLoanListRequest request) {
        try {
            HttpHeaders headers = getRequestHeadersCommon(null,
                    new PropertyObject(PPDOpenApiLoanListRequest.PARAM_NAME_PAGEINDEX, request.getPageIndex(), ValueTypeEnum.Int32),
                    new PropertyObject(PPDOpenApiLoanListRequest.PARAM_NAME_STARTDATETIME, request.getStartDateTime(), ValueTypeEnum.DateTime));
            ParameterizedTypeReference responseType = new ParameterizedTypeReference<PPDOpenApiLoanListResponse>() {
            };
            JSONObject jsonCredentials = new JSONObject();
            jsonCredentials.put(PPDOpenApiLoanListRequest.PARAM_NAME_PAGEINDEX, request.getPageIndex());
            jsonCredentials.put(PPDOpenApiLoanListRequest.PARAM_NAME_STARTDATETIME, request.getStartDateTime());
            ResponseEntity<PPDOpenApiLoanListResponse> responseEntity = callRetry(loanListUrl, jsonCredentials.toString(), HttpMethod.POST, headers, responseType);

            int statusCode = responseEntity.getStatusCode().value();
            if (statusCode != HttpStatus.OK.value()) {
                throw new IllegalStateException("获取散标可投标列表接口[" + loanListUrl + "]状态返回非法 statusCode=" + statusCode);
            }

            PPDOpenApiLoanListResponse response = responseEntity.getBody();
            return response;
        } catch (Exception e) {
            logger.error("获取散标可投标列表接口发生异常", e);
            throw new PPDOpenApiInvokeException(e.getMessage());
        }
    }

    /*public void invokeLoanList(PPDOpenApiLoanListRequest request) {
        try {
            Map<String, Object> headers = getRequestCommonHeaders(null,
                    new PropertyObject(PPDOpenApiLoanListRequest.PARAM_NAME_PAGEINDEX, request.getPageIndex(), ValueTypeEnum.Int32),
                    new PropertyObject(PPDOpenApiLoanListRequest.PARAM_NAME_STARTDATETIME, request.getStartDateTime(), ValueTypeEnum.DateTime));
            NettyClient client = new NettyClient();
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PPDOpenApiLoanListRequest.PARAM_NAME_PAGEINDEX, String.valueOf(request.getPageIndex()));
            paramMap.put(PPDOpenApiLoanListRequest.PARAM_NAME_STARTDATETIME, request.getStartDateTime());
            client.run(loanListUrl, NettyClient.getRequestMethod(paramMap, loanListUrl, "post", headers));
        } catch (Exception e) {
            logger.error("调用散标可投标列表接口发生异常", e);
            throw new PPDOpenApiInvokeException(e.getMessage());
        }
    }*/

    public void invokeLoanList(PPDOpenApiLoanListRequest request, FutureCallback<HttpResponse> futureCallback) {
        try {
            Map<String, String> headers = getRequestCommonHeaders(null,
                    new PropertyObject(PPDOpenApiLoanListRequest.PARAM_NAME_PAGEINDEX, request.getPageIndex(), ValueTypeEnum.Int32),
                    new PropertyObject(PPDOpenApiLoanListRequest.PARAM_NAME_STARTDATETIME, request.getStartDateTime(), ValueTypeEnum.DateTime));
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PPDOpenApiLoanListRequest.PARAM_NAME_PAGEINDEX, String.valueOf(request.getPageIndex()));
            paramMap.put(PPDOpenApiLoanListRequest.PARAM_NAME_STARTDATETIME, request.getStartDateTime());
            logger.debug("开始请求:url={},param={}", loanListUrl, JSONObject.toJSONString(paramMap));
            HttpAsyncClientUtil.post(loanListUrl, paramMap, headers, futureCallback);
        } catch (Exception e) {
            logger.error("调用散标可投标列表接口发生异常", e);
            throw new PPDOpenApiInvokeException(e.getMessage());
        }
    }

    //最多一批次查10个标详情
    public PPDOpenApiLoanListingDetailBatchResponse getListingDetailBatch(PPDOpenApiLoanListingDetailBatchRequest request) {
        try {
            HttpHeaders headers = getRequestHeadersCommon(null, new PropertyObject(PPDOpenApiLoanListingDetailBatchRequest.PARAM_NAME_LISTINGIDS, request.getListIds(), ValueTypeEnum.Other));
            ParameterizedTypeReference responseType = new ParameterizedTypeReference<PPDOpenApiLoanListingDetailBatchResponse>() {
            };
            JSONObject jsonCredentials = new JSONObject();
            jsonCredentials.put(PPDOpenApiLoanListingDetailBatchRequest.PARAM_NAME_LISTINGIDS, request.getListIds());
            ResponseEntity<PPDOpenApiLoanListingDetailBatchResponse> responseEntity = callRetry(batchListingInfosUrl, jsonCredentials.toString(), HttpMethod.POST, headers, responseType);

            int statusCode = responseEntity.getStatusCode().value();
            if (statusCode != HttpStatus.OK.value()) {
                throw new IllegalStateException("批量获取散标详情接口[" + batchListingInfosUrl + "]状态返回非法 statusCode=" + statusCode);
            }

            PPDOpenApiLoanListingDetailBatchResponse response = responseEntity.getBody();
            return response;
        } catch (Exception e) {
            logger.error("批量获取散标详情接口发生异常", e);
            throw new PPDOpenApiInvokeException(e.getMessage());
        }
    }
}
