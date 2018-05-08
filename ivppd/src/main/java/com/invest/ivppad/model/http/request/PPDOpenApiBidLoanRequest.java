package com.invest.ivppad.model.http.request;

import lombok.Data;

/**
 * Created by xugang on 2017/8/1.
 */
@Data
public class PPDOpenApiBidLoanRequest extends PPDOpenApiBaseRequest {
    public static final String PARAM_NAME_LISTINGID = "ListingId";
    public static final String PARAM_NAME_AMOUNT = "Amount";
    public static final String PARAM_NAME_USECOUPON = "UseCoupon";

    private int listingId; //必填,散标列表编号
    private int amount; //必填,投标金额
    private String useCoupon; //true：使用优惠券投标 false ：不使用优惠券投标 null ： 不使用优惠券

}
