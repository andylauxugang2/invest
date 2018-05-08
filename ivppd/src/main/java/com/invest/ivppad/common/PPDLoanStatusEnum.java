package com.invest.ivppad.common;

/**
 * 0 :流标 1:满标 2: 投标中 3 :借款成功（成功 || 成功已还清） 4: 审核失败 5 :撤标
 * Created by xugang on 2016/11/1.
 */
public enum PPDLoanStatusEnum {

    waste(0, "流标"),
    full(1, "满标"),
    loaning(2, "投标中"),
    loanSuccess(3, "借款成功（成功/成功已还清）"),
    verifyFailed(4, "审核失败"),
    cancel(5, "撤标"),
    unknown(-1, "unknown");

    private int code;
    private String desc;

    PPDLoanStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PPDLoanStatusEnum findByCode(int code) {
        for (PPDLoanStatusEnum item : PPDLoanStatusEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return unknown;
    }
}
