package com.invest.ivppad.model.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * {
 * "LoanInfos": [
 * {
 * "ListingId": 23886150,
 * "Title": "手机app用户的借款",
 * "CreditCode": "A",
 * "Amount": 1000,
 * "Rate": 12,
 * "Months": 12,
 * "PayWay": 0,
 * "Remainfunding":100
 * }
 * ],
 * "Result": 1,
 * "ResultMessage": "查询成功",
 * "ResultCode": null
 * }
 * Created by xugang on 17/01/16.
 */
@Data
public class PPDOpenApiLoanListResponse extends PPDOpenApiLoanBaseResponse {

    @JsonProperty("LoanInfos")
    private List<LoanInfo> loanInfoList;

    @Data
    public static class LoanInfo {
        @JsonProperty("ListingId")
        private int listingId; //列表ID
        @JsonProperty("Title")
        private String title; //标的标题
        @JsonProperty("CreditCode")
        private String creditCode; //标的级别
        @JsonProperty("Amount")
        private int Amount; //借款金额
        @JsonProperty("Rate")
        private double rate; //利率
        @JsonProperty("Months")
        private int months; //期限
        @JsonProperty("PayWay")
        private int payWay; //还款方式(0:等额本息(按月还款) 1:一次性还本付息)
        @JsonProperty("RemainFunding")
        private int remainFunding; //可投标金额
        @JsonProperty("PreAuditTime")
        private String preAuditTime; //可投标金额
    }
}