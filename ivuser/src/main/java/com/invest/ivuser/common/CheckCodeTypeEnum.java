package com.invest.ivuser.common;

/**
 * 1-用户注册，2-用户忘记密码
 * Created by xugang on 2017/7/29.
 */
public enum CheckCodeTypeEnum {
    SMS(1, "短信"), MAIL(2, "邮件"), UNKNOWN(-1, "unknown");

    private int code;
    private String desc;

    CheckCodeTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static CheckCodeTypeEnum findByCode(int code) {
        for (CheckCodeTypeEnum item : CheckCodeTypeEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return UNKNOWN;
    }
}
