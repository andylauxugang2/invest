package com.invest.ivppdgateway.model.request;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserBidLoanReq {
    private String userName;//ppd用户名

    private Integer listingId; //非必填
    private Integer amount; //投标金额 元
    private Boolean useCoupon; //是否使用优惠券

}
