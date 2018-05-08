package com.invest.ivuser.common;

/**
 * 按照code排序 优先级由低到高
 * Created by xugang on 2017/7/29.
 */
public enum UserPolicyTypeEnum {
    SYS_LOAN_POLICY((short)1, "系统散标策略"),
    USER_LOAN_POLICY((short)2, "用户自定义散标策略"),
    TRACER((short)3, "跟踪");

    private short code;
    private String desc;

    UserPolicyTypeEnum(short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public short getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static UserPolicyTypeEnum findByCode(short code) {
        for (UserPolicyTypeEnum item : UserPolicyTypeEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }
}
