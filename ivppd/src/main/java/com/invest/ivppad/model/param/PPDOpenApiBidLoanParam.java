package com.invest.ivppad.model.param;

import lombok.Data;

/**
 * Created by xugang on 16/11/2.
 */
@Data
public class PPDOpenApiBidLoanParam extends PPDOpenApiBaseParam {
    private static final long serialVersionUID = -3148889180208129245L;
    public static final String USECOUPON_VALUE_TRUE = "true";
    public static final String USECOUPON_VALUE_FALSE = "false";

    public static final String EXT_PARAM_NAME_PAGEINDEX = "pageIndex";

    private int listingId; //必填
    private int amount; //投标金额 元
    private String useCoupon; //true：使用优惠券投标 false ：不使用优惠券投标 null ： 无优惠券

}
