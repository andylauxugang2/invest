package com.invest.ivuser.common;

/**
 * Created by xugang on 2017/7/29.
 */
public enum UserPolicyStatusEnum {
    start((short)1, "开启"), stop((short)2, "停止"), drop((short)3, "废弃"),UNKNOWN((short)-1, "unknown");

    private short code;
    private String desc;

    UserPolicyStatusEnum(short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public short getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static UserPolicyStatusEnum findByCode(short code) {
        for (UserPolicyStatusEnum item : UserPolicyStatusEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return UNKNOWN;
    }
}
