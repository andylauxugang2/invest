package com.invest.ivusergateway.common.constants;

/**
 * Created by xugang on 2017/7/28.
 */
public enum ClientTypeEnum {

    IOS("1", "ios"), ANDROID("2", "android"), PC("3", "pc"), UNKNOWN("-1", "unknown");

    private String code;
    private String name;

    ClientTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ClientTypeEnum findByCode(String code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (ClientTypeEnum item : ClientTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return UNKNOWN;
    }
}
