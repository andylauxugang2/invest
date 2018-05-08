package com.invest.ivppad.common;

/**
 * 学习形式
 * Created by xugang on 2016/11/1.
 */
public enum PPDStudyStyleEnum {

    common(PPDBinExpConstants.B14, "普通"),
    hanshou(PPDBinExpConstants.B15, "函授"),
    netEducation(PPDBinExpConstants.B16, "网络教育"),
    zikao(PPDBinExpConstants.B17, "自考"),
    chengren(PPDBinExpConstants.B18, "成人"),
    unknown((short)0, "unknown");

    private long code;
    private String desc;

    PPDStudyStyleEnum(long code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public long getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PPDStudyStyleEnum findByCode(long code) {
        for (PPDStudyStyleEnum item : PPDStudyStyleEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }

    public static PPDStudyStyleEnum findByDesc(String desc) {
        for (PPDStudyStyleEnum item : PPDStudyStyleEnum.values()) {
            if (item.getDesc().equals(desc)) {
                return item;
            }
        }
        return unknown;
    }

}
