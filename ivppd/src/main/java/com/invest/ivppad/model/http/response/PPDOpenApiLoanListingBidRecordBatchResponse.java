package com.invest.ivppad.model.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.invest.ivcommons.util.format.DateFormatUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * {
 * "ListingBidsInfos": [
 * {
 * "ListingId": 100001,
 * "Bids": [
 * {
 * "LenderName": "test1",
 * "BidAmount": 50,
 * "BidDateTime": "2016-12-22T10:23:47.16"
 * },
 * {
 * "LenderName": "test2",
 * "BidAmount": 50,
 * "BidDateTime": "2016-12-22T10:27:57.47"
 * },
 * {
 * "LenderName": "test3",
 * "BidAmount": 100,
 * "BidDateTime": "2016-12-22T10:28:11.517"
 * }
 * ]
 * }
 * ],
 * "Result": 1,
 * "ResultMessage": "查询成功",
 * "ResultCode": null
 * }
 * 一个标 有多个投资人 [投资金额 投资时间]
 * Created by xugang on 17/01/16.
 */
@Data
public class PPDOpenApiLoanListingBidRecordBatchResponse extends PPDOpenApiLoanBaseResponse {

    @JsonProperty("ListingBidsInfos")
    private List<ListingBidDetail> loanListingBidDetailList;

    @Data
    public static class ListingBidDetail {
        @JsonProperty("ListingId")
        private int listingId; //标编号
        @JsonProperty("Bids")
        private List<BidDetail> bidDetailList; //投标详情
    }


    public static class BidDetail {
        @Getter
        @Setter
        @JsonProperty("LenderName")
        private String listingId; //投资人
        @Getter
        @Setter
        @JsonProperty("BidAmount")
        private double bidAmount; //投标金额
        @Getter
        @JsonProperty("BidDateTime")
        private String bidDateTime; //投标时间 2016-12-22T10:28:11.517

        //自定义
        private Date bidDate;
        
        public void setBidDateTime(String bidDateTime) {
            this.bidDateTime = bidDateTime;
            this.bidDate = DateFormatUtil.convertStr2Date(this.bidDateTime, DateFormatUtil.PATTERN_UTC_NO_ZONE_WITHMILLIS);
        }

        public Date getBidDate() {
            return bidDate;
        }

    }
}