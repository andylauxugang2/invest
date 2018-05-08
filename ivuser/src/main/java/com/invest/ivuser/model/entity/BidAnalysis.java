package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 逾期分析汇总表
 * Created by xugang on 2017/8/3.
 */
@Data
public class BidAnalysis extends BaseEntity {
    private static final long serialVersionUID = 1548916147202959751L;

    private String month; //统计年月份
    private Long userId;
    private String username;
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
