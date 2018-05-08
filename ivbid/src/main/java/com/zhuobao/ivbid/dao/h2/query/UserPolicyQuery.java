package com.zhuobao.ivbid.dao.h2.query;

import lombok.Data;

/**
 * Created by xugang on 2017/8/10.
 */
@Data
public class UserPolicyQuery {
    private Integer month;
    private Double rate;
    private Integer amount;

    private Integer age;
    private Short sex;

    private Integer loanerSuccessCount;
    private Integer wasteCount;
    private Integer normalCount;
    private Integer overdueLessCount;
    private Integer overdueMoreCount;
    private Integer totalPrincipal;
    private Integer owingPrincipal;
    private Integer amountToReceive;
    private Integer amountOwingTotal;
    private Integer lastSuccessBorrowDays;
    private Integer registerBorrowMonths;
    private Double owingHighestDebtRatio;
    private Double amtDebtRat;

    private long creditCode; //魔镜等级打标值
    private long thirdAuthInfo; //第三方认证打标值
    private long certificate; //学历认证打标值
    private long studyStyle; //学习形式打标值
    private long graduateSchoolType; //毕业学校分类打标值
}
