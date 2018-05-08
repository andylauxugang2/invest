package com.invest.ivuser.model.vo;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserVO {

    public static final String MONEY_ZERO = "￥0.00";

    private String mobile;
    private String password;
    private String referrerMobile;//推荐人手机号
    private String nick;
    private String userToken;
    private String headImgOrg;
    private String headImg;
    private String lastLoginTimeFormat;

    private String zhuobaoBalance = MONEY_ZERO; //捉宝币 余额

    private int bidCountToday; //今日投标个数
    private String incomeToday = MONEY_ZERO; //今日收益
    private int sysLoanPolicyCount; //系统散标策略个数
    private int loanPolicyCount; //自定义散标策略个数
    private int overdueTodayCount; //今日逾期数
    private int bindUserCount; //绑定用户数

}
