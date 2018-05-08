package com.invest.ivppad.model.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * "Result": 0,
 * "ResultMessage": "null",
 * "ListingId": "1123123",
 * "Amount": "150",
 * "ParticipationAmount": "50",
 * "CouponAmount": 150,
 * "CouponStatus": 1
 *
 * "message": "不允许重复投标",
 * "message": "已满标",
 * Created by xugang on 17/01/16.
 */
@Data
public class PPDOpenApiBidLoanResponse extends PPDOpenApiLoanBaseResponse {

    @JsonProperty("ListingId")
    private int listingId;
    @JsonProperty("Amount")
    private int amount; //投标金额
    @JsonProperty("ParticipationAmount")
    private int participationAmount; //实际投标金额
    @JsonProperty("CouponAmount")
    private Double couponAmount; //优惠券金额
    @JsonProperty("CouponStatus")
    private int couponStatus; //优惠券使用状态 0：未使用优惠券 1：成功使用优惠券 2：没有优惠券 @see PPDConponStatusEnum

    private static final int RESULT_SUCESS = 0; //返回码 0：成功 -1：异常(eg 时间跨度不能超过31天)
    public boolean success() {
        if(RESULT_SUCESS == super.getResult()){
            return true;
        }
        return false;
    }
}