package com.invest.ivuser.model.vo;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class ThirdUserInfoVO {

    public static final String MONEY_ZERO = "￥0.00";
    public static final String ERROR_THIRDUSERBALANCE = "账户余额获取异常";

    private String thirdUserUUID;//推荐人手机号
    private String thirdUserBalance;

}
