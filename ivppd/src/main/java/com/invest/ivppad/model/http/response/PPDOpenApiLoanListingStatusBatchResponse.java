package com.invest.ivppad.model.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.invest.ivppad.common.PPDLoanStatusEnum;
import lombok.Data;

import java.util.List;

/**
 * {
 * "Infos": [
 * {
 * "ListingId": 100000,
 * "Status": 3
 * }
 * ],
 * "Result": 1,
 * "ResultMessage": "查询成功",
 * "ResultCode": null
 * }
 * Created by xugang on 17/01/16.
 */
@Data
public class PPDOpenApiLoanListingStatusBatchResponse extends PPDOpenApiLoanBaseResponse {

    @JsonProperty("Infos")
    private List<LoanListingStatus> loanListingStatusList;


    @Data
    public static class LoanListingStatus {
        @JsonProperty("ListingId")
        private int listingId; //列表ID
        @JsonProperty("Status")
        private int status; //0 :流标 1:满标 2: 投标中 3 :借款成功（成功 || 成功已还清） 4: 审核失败 5 :撤标 @see PPDLoanStatusEnum

        //自定义
        private String statusDesc;

        public void setStatus(int status) {
            this.status = status;
            this.statusDesc = PPDLoanStatusEnum.findByCode(status).getDesc();
        }

    }
}