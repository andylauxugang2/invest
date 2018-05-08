package com.invest.ivppad.model.result;

import com.invest.ivppad.model.http.response.*;
import com.invest.ivuser.model.entity.UserLoanRecord;
import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/8/1.
 */
@Data
public class PPDOpenApiLoanResult extends PPDOpenApiResult {
    //可投散标列表
    private List<PPDOpenApiLoanListResponse.LoanInfo> loanInfoList;
    //可投散标列表详情列表
    private List<PPDOpenApiLoanListingDetailBatchResponse.LoanListingDetail> loanListingDetailList;
    //批量查询散标投资状态
    private List<PPDOpenApiLoanListingStatusBatchResponse.LoanListingStatus> loanListingStatusList;
    //获取用户投标记录
    private PPDOpenApiLoanUserBidListResponse userLoanBidList;
    //获取用户投标记录
    private List<PPDOpenApiLoanListingBidRecordBatchResponse.ListingBidDetail> loanListingBidRecordList;
    //获取用户投资列表的还款情况
    private List<PPDOpenApiLoanLenderRepaymentResponse.LoanPaymentDetail> userLoanPaymentDetailList;
    //用户投标记录
    private UserLoanRecord userLoanRecord;
}
