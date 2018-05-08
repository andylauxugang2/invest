package com.invest.ivuser.common;

/**
 * 标的 风险等级
 * Created by xugang on 2017/7/29.
 */
public enum LoanRiskLevelEnum {
    low((short) 1, "低风险"), mid((short) 2, "中风险"), high((short) 3, "高风险"), sys((short) 4, "系统评估");

    private short code;
    private String desc;

    LoanRiskLevelEnum(short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public short getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static LoanRiskLevelEnum findByCode(short code) {
        for (LoanRiskLevelEnum item : LoanRiskLevelEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }
}
