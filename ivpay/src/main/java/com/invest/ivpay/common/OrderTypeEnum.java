package com.invest.ivpay.common;

/**
 * Created by xugang on 2017/7/28.
 */
public enum OrderTypeEnum {

    recharge((short) 100, "充值");

    private short code;
    private String name;

    OrderTypeEnum(short code, String name) {
        this.code = code;
        this.name = name;
    }

    public short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static OrderTypeEnum findByCode(short code) {
        for (OrderTypeEnum item : OrderTypeEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }
}
