package com.invest.ivppad.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivppad.TestBase;
import com.invest.ivppad.model.http.request.PPDOpenApiLoanLenderRepaymentRequest;
import com.invest.ivppad.model.http.request.PPDOpenApiLoanListingDetailBatchRequest;
import com.invest.ivppad.model.http.response.PPDOpenApiLoanLenderRepaymentResponse;
import com.invest.ivppad.model.http.response.PPDOpenApiLoanListingDetailBatchResponse;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xugang on 2017/11/2.do best.
 */
public class PPDOpenApiLoanManagerTest extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(PPDOpenApiLoanManagerTest.class);

    @Autowired
    private PPDOpenApiLoanManager ppdOpenApiLoanManager;

    @Autowired
    private PPDOpenApiLoanListManager ppdOpenApiLoanListManager;

    public void testGetLoanList() throws Exception {

    }

    public void testInvokeLoanList() throws Exception {

    }

    @Test
    public void testGetListingDetailBatch() throws Exception {
        PPDOpenApiLoanListingDetailBatchRequest request = new PPDOpenApiLoanListingDetailBatchRequest();
        List<Integer> listIds = new ArrayList<>();
        listIds.add(82009332);
        request.setListIds(listIds);
        PPDOpenApiLoanListingDetailBatchResponse response = ppdOpenApiLoanListManager.getListingDetailBatch(request);
        logger.info("response=" + JSONObject.toJSONString(response));
        logger.info("response=" + JSONObject.toJSONString(response.getLoanListingDetailList()));
    }

    public void testGetListingStatusBatch() throws Exception {

    }

    public void testGetUserBidList() throws Exception {

    }

    public void testGetLoanListingBidList() throws Exception {

    }

    @Test
    public void testGetLenderRepayment() throws Exception {
        String accessToken = "4a400da5-92f0-4b97-8bc2-8034fa264948";
        PPDOpenApiLoanLenderRepaymentRequest repaymentRequest = new PPDOpenApiLoanLenderRepaymentRequest();
        repaymentRequest.setOrderId(null);
        repaymentRequest.setListingId(75101654);
        PPDOpenApiLoanLenderRepaymentResponse response = ppdOpenApiLoanManager.getLenderRepayment(accessToken, repaymentRequest);
        logger.info("response=" + JSONObject.toJSONString(response));
        logger.info("response=" + JSONObject.toJSONString(response.getLoanPaymentDetailList()));
    }

    public void testBidLoan() throws Exception {

    }

    public void testInvokeBidLoan() throws Exception {

    }
}