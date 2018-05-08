package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

/**
 * 逾期分析汇总表 策略维度
 * Created by xugang on 2017/8/3.
 */
@Data
public class BidAnalysisPolicy extends BaseEntity {

    private static final long serialVersionUID = 1815445209411521180L;
    private Long userId;
    private String username;
    private String policyName;
    private Long policyId;
    private Integer policyType;

    private Integer bidCountTotal;
    private Integer bidAmountTotal;
    private Double bidRateAvg;
    private Double bidMonthAvg;
    private Double bidAgeAvg;
    private Double bidLenderCountAvg;

    private Integer overdue10Days;
    private Integer overdue10Total;
    private Integer overdue30Days;
    private Integer overdue30Total;
    private Integer overdue60Days;
    private Integer overdue60Total;
    private Integer overdue90Days;
    private Integer overdue90Total;

    private Double overduePrincipal;
}
