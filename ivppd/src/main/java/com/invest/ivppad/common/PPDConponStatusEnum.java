package com.invest.ivppad.common;

/**
 * 优惠券使用状态 0：未使用优惠券 1：成功使用优惠券 2：没有优惠券
 * Created by xugang on 2016/11/1.
 */
public enum PPDConponStatusEnum {

    no_use(0, "未使用优惠券"),
    use_success(1, "成功使用优惠券"),
    no_exists(2, "没有优惠券"),
    unknown(-1, "unknown");

    private int code;
    private String desc;

    PPDConponStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PPDConponStatusEnum findByCode(int code) {
        for (PPDConponStatusEnum item : PPDConponStatusEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return unknown;
    }
}
