package com.invest.ivppad.model.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * {
 * "Result": 0,
 * "ResultMessage": "null",
 * "TotalPages": "1",
 * "TotalRecord": "20",
 * "BidList": {
 * "Title": "测试使用",
 * "ListingId": "223423",
 * "Months": "10",
 * "Rate": "10",
 * "Amount": "10000",
 * "BidAmount": "80"
 * }
 * }
 * Created by xugang on 17/01/16.
 */
@Data
public class PPDOpenApiLoanUserBidListResponse extends PPDOpenApiLoanBaseResponse {

    @JsonProperty("TotalPages")
    private int totalPages; //总页数
    @JsonProperty("TotalRecord")
    private int totalRecord; //总记录数
    @JsonProperty("BidList")
    private List<LoanBid> loanBidList;

    @Data
    public static class LoanBid {
        @JsonProperty("ListingId")
        private int listingId; //标编号
        @JsonProperty("Title")
        private String title; //标题
        @JsonProperty("Months")
        private int months; //期数
        @JsonProperty("Rate")
        private double rate; //利率
        @JsonProperty("Amount")
        private double amount; //标的金额
        @JsonProperty("BidAmount")
        private int bidAmount; //投标金额
    }

    private static final int RESULT_SUCESS = 0; //返回码 0：成功 -1：异常(eg 时间跨度不能超过31天)
    public boolean success() {
        if(RESULT_SUCESS == super.getResult()){
            return true;
        }
        return false;
    }
}