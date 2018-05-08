package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

/**
 * 用户投标记录
 * Created by xugang on 2017/8/3.
 */
@Data
public class UserLoanRecord extends BaseEntity {

    private static final long serialVersionUID = 7170366913076522676L;

    public static final short DOWNDETAILFLAG_YES = 1;
    public static final short DOWNDETAILFLAG_NO = 0;

    private Integer loanId; //标的唯一标识 如listingId
    private Long userId; //标的唯一标识 如listingId
    private Integer amount; //投标金额
    private Integer participationAmount; //实际投标金额
    private Double couponAmount; //优惠券金额
    private byte couponStatus; //优惠券使用状态 0：未使用优惠券 1：成功使用优惠券 2：没有优惠券 @see PPDConponStatusEnum

    private String username;
    private Long policyId;
    private Short policyType;

    private Short downDetailFlag; //1-下载过详情,0-未下载过详情
    private Short downRepaymentFlag; //1-下载过详情,0-未下载过详情

    //额外
    private String policyName;

}
