package com.invest.ivppad.model.http.request;

import lombok.Data;

/**
 * Created by xugang on 2017/8/1.
 */
@Data
public class PPDOpenApiLoanLenderRepaymentRequest extends PPDOpenApiBaseRequest {
    public static final String PARAM_NAME_LISTINGID = "ListingId";
    public static final String PARAM_NAME_ORDERID = "OrderId";

    private int listingId; //必填,散标列表编号
    private String orderId; //非必填,还款期数,参数为空时，获取当前列表所有期数；参数不为空时，获取传参的期数

}
