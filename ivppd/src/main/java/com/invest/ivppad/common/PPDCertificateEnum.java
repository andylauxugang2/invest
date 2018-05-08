package com.invest.ivppad.common;

/**
 * 学历
 * Created by xugang on 2016/11/1.
 */
public enum PPDCertificateEnum {

    high_school(PPDBinExpConstants.B1, "专科"),
    junior_college(PPDBinExpConstants.B2, "本科"),
    regular_college(PPDBinExpConstants.B3, "研究生"),
    postgraduate(PPDBinExpConstants.B4, "硕士"),
    master(PPDBinExpConstants.B5, "博士"),
    unknown((short)0, "unknown");

    private long code;
    private String desc;

    PPDCertificateEnum(long code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public long getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PPDCertificateEnum findByCode(long code) {
        for (PPDCertificateEnum item : PPDCertificateEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }

    public static PPDCertificateEnum findByDesc(String desc) {
        for (PPDCertificateEnum item : PPDCertificateEnum.values()) {
            if (desc.contains(item.getDesc())) { //只要包含就算 如 专科(高职)
                return item;
            }
        }
        return unknown;
    }
}
