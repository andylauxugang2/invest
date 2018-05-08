package com.invest.ivusergateway.model.vo;

import lombok.Data;

/**
 * Created by xugang on 2017/8/16.
 */
@Data
public class UserLoanRecordVO {

    private Long id;
    private Long userId; //标的唯一标识 如listingId
    private String username;
    private Long policyId;
    private String policyType;
    private String name;

    private Integer loanId; //标的唯一标识 如listingId
    private String amount; //投标金额
    private String participationAmount; //实际投标金额
    private String couponAmount; //优惠券金额
    private String couponStatus; //优惠券使用状态 0：未使用优惠券 1：成功使用优惠券 2：没有优惠券 @see PPDConponStatusEnum

    private String createTime;
}
