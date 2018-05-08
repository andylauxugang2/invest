package com.invest.ivppad.common;

import org.apache.commons.lang.enums.EnumUtils;

import java.util.Map;

/**
 * 标的模型等级
 * Created by xugang on 2016/11/1.
 */
public enum PPDCreditCodeEnum {

    AAA(PPDBinExpConstants.B6, "AAA"),
    AA(PPDBinExpConstants.B7, "AA"),
    A(PPDBinExpConstants.B8, "A"),
    B(PPDBinExpConstants.B9, "B"),
    C(PPDBinExpConstants.B10, "C"),
    D(PPDBinExpConstants.B11, "D"),
    E(PPDBinExpConstants.B12, "E"),
    F(PPDBinExpConstants.B13, "F"),
    UNKNOWN((short)0, "unknown");

    private long code;
    private String desc;

    PPDCreditCodeEnum(long code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public long getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PPDCreditCodeEnum findByCode(long code) {
        for (PPDCreditCodeEnum item : PPDCreditCodeEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }

    public static PPDCreditCodeEnum findByDesc(String desc) {
        for (PPDCreditCodeEnum item : PPDCreditCodeEnum.values()) {
            if (item.getDesc().equalsIgnoreCase(desc)) {
                return item;
            }
        }
        return UNKNOWN;
    }

    public static void main(String[] args) {
        Map<Long, String> map = EnumUtils.getEnumMap(PPDCreditCodeEnum.class);
        System.out.println(map);
    }
}
