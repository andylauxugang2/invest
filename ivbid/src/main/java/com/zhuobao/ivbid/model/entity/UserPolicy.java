package com.zhuobao.ivbid.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserPolicy implements Serializable {
    private static final long serialVersionUID = 7452287252716909568L;

    public static final int MIN_VALUE = 0;
    public static final double MIN_DOUBLE_VALUE = 0;
    public static final int MAX_VALUE_MONEY = 100000000;
    public static final int MAX_VALUE_MONTH = 60;
    public static final double MAX_VALUE_RATE = 100;
    public static final double MAX_VALUE_OWINGHIGHESTDEBTRATIO = 10;
    public static final double MAX_VALUE_AMOUNTHIGHESTDEBTRATIO = 10;
    public static final int MAX_VALUE_AGE = 200;
    public static final int MAX_VALUE_AMOUNT = 10000000;
    public static final int MAX_VALUE_TIMES = 10000; //次数
    public static final int MAX_VALUE_DAYS = 10000; //天数
    public static final int MAX_VALUE_REGISTERBORROWMONTHS_MONTH = 10000; //月数


    private Long id;

    private Short type; //策略类型,系统散标,散标自定义
    private Long policyId; //策略id

    private Long userId; //策略所属用户id
    private String username; //ppd username
    private Integer monthBegin = MIN_VALUE;
    private Integer monthEnd = MAX_VALUE_MONTH;
    private Integer amountBegin = MIN_VALUE; //借款起始金额
    private Integer amountEnd = MAX_VALUE_MONEY; //借款截止金额
    private Double rateBegin = MIN_DOUBLE_VALUE;
    private Double rateEnd = MAX_VALUE_RATE;

    private Integer ageBegin = MIN_VALUE;
    private Integer ageEnd = MAX_VALUE_AGE;
    private Short sex;

    private long creditCode; //魔镜等级打标值
    private long thirdAuthInfo; //第三方认证打标值
    private long certificate; //学历认证打标值
    private long studyStyle; //学习形式打标值
    private long graduateSchoolType; //毕业学校分类打标值

    private Integer loanerSuccessCountBegin = MIN_VALUE; //成功借款次数
    private Integer loanerSuccessCountEnd = MAX_VALUE_TIMES; //成功借款次数
    private Integer wasteCountBegin = MIN_VALUE;
    private Integer wasteCountEnd = MAX_VALUE_TIMES;
    private Integer normalCountBegin = MIN_VALUE;
    private Integer normalCountEnd = MAX_VALUE_TIMES;
    private Integer overdueLessCountBegin = MIN_VALUE;
    private Integer overdueLessCountEnd = MAX_VALUE_TIMES;
    private Integer overdueMoreCountBegin = MIN_VALUE;
    private Integer overdueMoreCountEnd = MAX_VALUE_TIMES;
    private Integer totalPrincipalBegin = MIN_VALUE;
    private Integer totalPrincipalEnd = MAX_VALUE_MONEY;
    private Integer owingPrincipalBegin = MIN_VALUE;
    private Integer owingPrincipalEnd = MAX_VALUE_MONEY;
    private Integer amountToReceiveBegin = MIN_VALUE;
    private Integer amountToReceiveEnd = MAX_VALUE_MONEY;
    private Integer lastSuccessBorrowDaysBegin = MIN_VALUE;
    private Integer lastSuccessBorrowDaysEnd = MAX_VALUE_DAYS;
    private Integer registerBorrowMonthsBegin = MIN_VALUE;
    private Integer registerBorrowMonthsEnd = MAX_VALUE_REGISTERBORROWMONTHS_MONTH;
    private Integer amountOwingTotalBegin = MIN_VALUE;
    private Integer amountOwingTotalEnd = MAX_VALUE_MONEY;

    private Double owingHighestDebtRatioBegin = MIN_DOUBLE_VALUE;
    private Double owingHighestDebtRatioEnd = MAX_VALUE_OWINGHIGHESTDEBTRATIO;
    private Double amtDebtRatBg = MIN_DOUBLE_VALUE;
    private Double amtDebtRatEd = MAX_VALUE_AMOUNTHIGHESTDEBTRATIO;

    private Integer bidAmount; //投标金额
}
