package com.invest.ivusergateway.model.vo;

import lombok.Data;

/**
 * Created by xugang on 2017/8/16.
 */
@Data
public class OverdueAnalysisVO {

    private static final String ZERO_RATE = "-";

    private Long id;
    private String month; //统计年月份
    private Long userId;
    private String username;
    private String bidCountTotal = ZERO_RATE;
    private String bidAmountTotal = ZERO_RATE; //￥13,132
    private String bidRateAvg = ZERO_RATE; //22.12%
    private String bidMonthAvg = ZERO_RATE; //12.12
    private String bidAgeAvg = ZERO_RATE; //23.12
    private String bidLenderCountAvg = ZERO_RATE; //56.2

    private String overdue10Days = ZERO_RATE; //7
    private String overdue10Total = ZERO_RATE; //7
    private String overdue10DaysRate = ZERO_RATE; //3.06%
    private String overdue30Days = ZERO_RATE;
    private String overdue30Total = ZERO_RATE; //7
    private String overdue30DaysRate = ZERO_RATE; //3.06%
    private String overdue60Days = ZERO_RATE;
    private String overdue60Total = ZERO_RATE; //7
    private String overdue60DaysRate = ZERO_RATE; //3.06%
    private String overdue90Days = ZERO_RATE;
    private String overdue90Total = ZERO_RATE; //7
    private String overdue90DaysRate = ZERO_RATE; //3.06%
}
