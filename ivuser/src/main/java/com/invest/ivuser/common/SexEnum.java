package com.invest.ivuser.common;

/**
 * Created by xugang on 2017/7/28.
 */
public enum SexEnum {

    male((short) 1, "男"), female((short) 0, "女");

    private short code;
    private String name;

    SexEnum(short code, String name) {
        this.code = code;
        this.name = name;
    }

    public short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static SexEnum findByCode(short code) {
        for (SexEnum item : SexEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }
}
