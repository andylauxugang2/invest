package com.invest.ivpay.common;

/**
 * Created by xugang on 2017/7/28.
 */
public enum OrderStatusEnum {

    bzj_confirming_pay((short) 0, "待专家确认支付"),
    bzj_confirmd_pay((short) 1, "专家已确认支付完成"),
    bzj_handled((short) 2, "专家已处理完成");

    private short code;
    private String name;

    OrderStatusEnum(short code, String name) {
        this.code = code;
        this.name = name;
    }

    public short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static OrderStatusEnum findByCode(short code) {
        for (OrderStatusEnum item : OrderStatusEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }
}
