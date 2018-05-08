package com.invest.ivusergateway.model.vo;

import lombok.Data;

/**
 * Created by xugang on 2017/8/16.
 */
@Data
public class OverdueAnalysisDetailVO {

    private static final String ZERO_RATE = "-";

    private Long userId;
    private String username;
    private String bidTime;
    private Integer listingId;
    private Long policyId;
    private String policyName = ZERO_RATE;
    private String month = ZERO_RATE;
    private String creditCode = ZERO_RATE;
    private String rate;
    private String amount;
    private String bidAmount = ZERO_RATE;
    private String overdueDays = ZERO_RATE; //逾期天数
    private String repay = ZERO_RATE; //已收本金 + 已收利息
    private String owing = ZERO_RATE; //未收本金 + 未收利息
    private String owingOverdue = ZERO_RATE; //逾期利息
}
