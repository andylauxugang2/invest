package com.invest.ivpay.common;

/**
 * Created by xugang on 2017/7/28.
 */
public enum PayStatusEnum {

    paid((short) 1, "未支付"),
    unpaid((short) 2, "已支付");

    private short code;
    private String name;

    PayStatusEnum(short code, String name) {
        this.code = code;
        this.name = name;
    }

    public short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static PayStatusEnum findByCode(short code) {
        for (PayStatusEnum item : PayStatusEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }
}
