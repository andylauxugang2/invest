package com.invest.ivuser.common;

/**
 * Created by xugang on 2016/11/1.
 */
public enum LoanOverdueTypeEnum {

    overdue_10(1, "逾期10天", 10),
    overdue_30(2, "逾期30天", 30),
    overdue_60(3, "逾期60天", 60),
    overdue_90(4, "逾期90天", 90);

    private int code;
    private String desc;
    private int value;

    LoanOverdueTypeEnum(int code, String desc, int value) {
        this.code = code;
        this.desc = desc;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public int getValue() {
        return value;
    }

    public static LoanOverdueTypeEnum findByCode(int code) {
        for (LoanOverdueTypeEnum item : LoanOverdueTypeEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }
}
