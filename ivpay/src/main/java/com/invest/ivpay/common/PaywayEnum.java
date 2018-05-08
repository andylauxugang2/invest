package com.invest.ivpay.common;

/**
 * Created by xugang on 2017/7/28.
 */
public enum PaywayEnum {

    alipay((short) 1, "支付宝"),
    wx((short) 2, "微信");

    private short code;
    private String name;

    PaywayEnum(short code, String name) {
        this.code = code;
        this.name = name;
    }

    public short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static PaywayEnum findByCode(short code) {
        for (PaywayEnum item : PaywayEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }
}
