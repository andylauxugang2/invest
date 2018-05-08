package com.invest.ivppad.model.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * "ListingPayment": [
 * {
 * "ListingId": 100036,
 * "OrderId": 3,
 * "DueDate": "2007-10-10",
 * "RepayDate": "2007-10-10",
 * "RepayPrincipal": 21.74,
 * "RepayInterest": 1.19,
 * "OwingPrincipal": 0,
 * "OwingInterest": 0,
 * "OwingOverdue": 0,
 * "OverdueDays": 0,
 * "RepayStatus": 1
 * }
 * ],
 * "Result": 1,
 * "ResultMessage": "查询成功",
 * }
 * 一个用户投资的标,其还款情况,逾期情况查看
 * response=[
 * {
 * "dueDate": "2017-10-10",
 * "listingId": 70734097,
 * "orderId": 1,
 * "overdueDays": -30,
 * "owingInterest": 0,
 * "owingOverdue": 0,
 * "owingPrincipal": 0,
 * "repayDate": "2017-09-10",
 * "repayInterest": 0.97,
 * "repayPrincipal": 5.47,
 * "repayStatus": 1
 * },
 * {
 * "dueDate": "2017-11-10",
 * "listingId": 70734097,
 * "orderId": 2,
 * "overdueDays": -61,
 * "owingInterest": 0,
 * "owingOverdue": 0,
 * "owingPrincipal": 0,
 * "repayDate": "2017-09-10",
 * "repayInterest": 0.87,
 * "repayPrincipal": 5.57,
 * "repayStatus": 1
 * },
 * {
 * "dueDate": "2017-12-10",
 * "listingId": 70734097,
 * "orderId": 3,
 * "overdueDays": -91,
 * "owingInterest": 0,
 * "owingOverdue": 0,
 * "owingPrincipal": 0,
 * "repayDate": "2017-09-10",
 * "repayInterest": 0.77,
 * "repayPrincipal": 5.67,
 * "repayStatus": 1
 * },
 * {
 * "dueDate": "2018-01-10",
 * "listingId": 70734097,
 * "orderId": 4,
 * "overdueDays": -121,
 * "owingInterest": 0,
 * "owingOverdue": 0,
 * "owingPrincipal": 0,
 * "repayDate": "2017-09-11",
 * "repayInterest": 0.67,
 * "repayPrincipal": 5.77,
 * "repayStatus": 1
 * },
 * {
 * "dueDate": "2018-02-10",
 * "listingId": 70734097,
 * "orderId": 5,
 * "overdueDays": -152,
 * "owingInterest": 0,
 * "owingOverdue": 0,
 * "owingPrincipal": 0,
 * "repayDate": "2017-09-11",
 * "repayInterest": 0.55,
 * "repayPrincipal": 5.89,
 * "repayStatus": 1
 * },
 * {
 * "dueDate": "2018-03-10",
 * "listingId": 70734097,
 * "orderId": 6,
 * "overdueDays": -180,
 * "owingInterest": 0,
 * "owingOverdue": 0,
 * "owingPrincipal": 0,
 * "repayDate": "2017-09-11",
 * "repayInterest": 0.46,
 * "repayPrincipal": 5.99,
 * "repayStatus": 1
 * },
 * {
 * "dueDate": "2018-04-10",
 * "listingId": 70734097,
 * "orderId": 7,
 * "overdueDays": -211,
 * "owingInterest": 0,
 * "owingOverdue": 0,
 * "owingPrincipal": 0,
 * "repayDate": "2017-09-11",
 * "repayInterest": 0.34,
 * "repayPrincipal": 6.1,
 * "repayStatus": 1
 * },
 * {
 * "dueDate": "2018-05-10",
 * "listingId": 70734097,
 * "orderId": 8,
 * "overdueDays": -241,
 * "owingInterest": 0,
 * "owingOverdue": 0,
 * "owingPrincipal": 0,
 * "repayDate": "2017-09-11",
 * "repayInterest": 0.23,
 * "repayPrincipal": 6.21,
 * "repayStatus": 1
 * },
 * {
 * "dueDate": "2018-06-10",
 * "listingId": 70734097,
 * "orderId": 9,
 * "overdueDays": -272,
 * "owingInterest": 0,
 * "owingOverdue": 0,
 * "owingPrincipal": 0,
 * "repayDate": "2017-09-11",
 * "repayInterest": 0.11,
 * "repayPrincipal": 6.33,
 * "repayStatus": 1
 * }
 * ]
 * Created by xugang on 17/01/16.
 */
@Data
public class PPDOpenApiLoanLenderRepaymentResponse extends PPDOpenApiLoanBaseResponse {

    @JsonProperty("ListingRepayment")
    private List<LoanPaymentDetail> loanPaymentDetailList; //标还款情况集合

    @Data
    public static class LoanPaymentDetail {
        @JsonProperty("ListingId")
        private int listingId; //标编号
        @JsonProperty("OrderId")
        private int orderId; //还款期数
        @JsonProperty("DueDate")
        private String dueDate; //应还日期 2007-10-10
        @JsonProperty("RepayDate")
        private String repayDate; //实际还款日期
        @JsonProperty("RepayPrincipal")
        private double repayPrincipal; //已收本金
        @JsonProperty("RepayInterest")
        private double repayInterest; //已收利息
        @JsonProperty("OwingPrincipal")
        private double owingPrincipal; //未收本金
        @JsonProperty("OwingInterest")
        private double owingInterest; //未收利息
        @JsonProperty("OwingOverdue")
        private double owingOverdue; //逾期利息
        @JsonProperty("OverdueDays")
        private int overdueDays; //逾期天数 0
        @JsonProperty("RepayStatus")
        private int repayStatus; //还款状态 0：等待还款 1：准时还款 2：逾期还款 3：提前还款 4：部分还款 @see PPDLoanRepayStatusEnum


    }
}