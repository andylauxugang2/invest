package com.invest.ivcommons.validate.model;

/**
 * Created by xugang on 2017/7/28.
 */
public enum ValidParamErrorEnum {
    NULL_ERROR("请输入%s"),
    INVALID_ERROR("请输入正确的%s"),
    LOGIN_PASSWORD_FORMAT_ERROR("登录密码必须是6-20位字母与数字的组合");

    private String desc;

    ValidParamErrorEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

}
