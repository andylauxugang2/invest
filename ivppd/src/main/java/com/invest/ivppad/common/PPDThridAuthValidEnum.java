package com.invest.ivppad.common;

/**
 * 第三方认证信息
 * Created by xugang on 2016/11/1.
 */
public enum PPDThridAuthValidEnum {

    certificateValid(PPDBinExpConstants.B19, "学历认证"),
    creditValid(PPDBinExpConstants.B20, "征信认证"),
    videoValid(PPDBinExpConstants.B21, "视频认证"),
    phoneValid(PPDBinExpConstants.B22, "手机认证"),
    nciicIdentityValid(PPDBinExpConstants.B23, "户籍认证"),
    educateValid(PPDBinExpConstants.B24, "学籍认证"),
    unknown((short)0, "unknown");

    private long code;
    private String desc;

    PPDThridAuthValidEnum(long code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public long getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PPDThridAuthValidEnum findByCode(long code) {
        for (PPDThridAuthValidEnum item : PPDThridAuthValidEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return unknown;
    }
}
