package com.invest.ivusergateway.model.vo;

import lombok.Data;

/**
 * Created by xugang on 2017/8/16.
 */
@Data
public class LoanPolicyVO {
    public static final String VALUE_NO_LIMIT = "无限制";

    private Long id;
    private String name; //策略名称 可重复
    private String validTime; //有效期
    private String riskLevel;
    private short riskLevelCode;
    private String createTime; //创建时间
    private String amount; //借款金额范围
    private String month;
    private String rate;
    private String age;
    private String sex;
    private String creditCode = VALUE_NO_LIMIT; //魔镜等级
    private String thirdAuthInfo = VALUE_NO_LIMIT; //第三方认证
    private String certificate = VALUE_NO_LIMIT; //学历认证
    private String studyStyle = VALUE_NO_LIMIT; //学习形式
    private String graduateSchoolType = VALUE_NO_LIMIT; //毕业学校分类
    private String loanerSuccessCount; //成功借款次数
    private String wasteCount;
    private String normalCount;
    private String overdueLessCount;
    private String overdueMoreCount;
    private String totalPrincipal;
    private String owingPrincipal;
    private String amountToReceive;
    private String amountOwingTotal;
    private String lastSuccessBorrowDays;
    private String registerBorrowMonths;
    private String owingHighestDebtRatio;
    private String amtDebtRat;

}
