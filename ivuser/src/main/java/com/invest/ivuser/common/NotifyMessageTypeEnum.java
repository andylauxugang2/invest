package com.invest.ivuser.common;

/**
 * Created by xugang on 2017/7/28.
 */
public enum NotifyMessageTypeEnum {

    activity((short) 1, "活动"),
    policy((short) 2, "策略"),
    security((short) 3, "安全");

    private short code;
    private String desc;

    NotifyMessageTypeEnum(short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public short getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static NotifyMessageTypeEnum findByCode(short code) {
        for (NotifyMessageTypeEnum item : NotifyMessageTypeEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }
}
