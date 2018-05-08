package com.invest.ivppad.biz.service.ppdopenapi;

import com.invest.ivppad.model.param.*;
import com.invest.ivppad.model.result.PPDOpenApiLoanResult;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

/**
 * 用户账户
 * Created by xugang on 2017/8/1.
 */
public interface LoanService {

    /**
     * 获取散标可投标列表
     * @param param
     * @return
     */
    PPDOpenApiLoanResult getLoanList(PPDOpenApiGetLoanListParam param);

    /**
     * 批量获取散标详情
     * @param param
     * @return
     */
    PPDOpenApiLoanResult getLoanListingDetailBatch(PPDOpenApiGetLoanListingDetailBatchParam param);

    /**
     * 批量查询散标投资状态
     * @param param
     * @return
     */
    PPDOpenApiLoanResult getLoanListingStatusBatch(PPDOpenApiGetLoanListingStatusBatchParam param);

    /**
     * 获取用户投标记录
     * @param param
     * @return
     */
    PPDOpenApiLoanResult getUserLoanBidList(PPDOpenApiGetUserLoanBidListParam param);

    /**
     * 批量获取散标投资记录 一个标有多个投资人 [投资金额,投资时间]
     * @param param
     * @return
     */
    PPDOpenApiLoanResult getLoanListingBidRecordList(PPDOpenApiGetLoanListingBidRecordBatchParam param);

    /**
     * 获取用户投资列表的还款情况 逾期情况 等
     * @param param 可根据还款期数查询 指定期数的还款明细
     * @return
     */
    PPDOpenApiLoanResult getUserLoanRepaymentDetail(PPDOpenApiGetUserLoanRepaymentParam param);

    /**
     * 投标
     * @param param
     * @return
     */
    PPDOpenApiLoanResult bidLoan(PPDOpenApiBidLoanParam param);

    /**
     * 投标
     * @param param
     * @return
     */
    PPDOpenApiLoanResult bidLoan(PPDOpenApiBidLoanParam param, FutureCallback<HttpResponse> futureCallback);
}
