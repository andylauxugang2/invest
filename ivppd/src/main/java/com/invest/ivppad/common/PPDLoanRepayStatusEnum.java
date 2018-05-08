package com.invest.ivppad.common;

/**
 * Created by xugang on 2016/11/1.
 */
public enum PPDLoanRepayStatusEnum {

    waiting(0, "等待还款"),
    ontime(1, "准时还款"),
    overdue(2, "逾期还款"),
    advanced(3, "提前还款"),
    parted(4, "部分还款"),
    unknown(-1, "unknown");

    private int code;
    private String desc;

    PPDLoanRepayStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PPDLoanRepayStatusEnum findByCode(int code) {
        for (PPDLoanRepayStatusEnum item : PPDLoanRepayStatusEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return unknown;
    }
}
