package com.invest.ivuser.model.vo;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class LoanRepaymentDetailVO {

    private Long userId;
    private String username;
    private String bidTime;
    private Integer listingId;
    private Integer month;
    private String creditCode;
    private Double rate;
    private Integer amount;
    private Integer bidAmount;

    private Long policyId;
    private String policyName;
    private String policyType;

    private Integer overdueDays; //逾期天数
    private Double repay; //已收本金 + 已收利息
    private Double owing; //未收本金 + 未收利息
    private Double owingOverdue; //逾期利息

}
