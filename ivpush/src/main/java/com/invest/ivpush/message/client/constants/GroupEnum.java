package com.invest.ivpush.message.client.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * 0-通用，1-会员，2-交易，3-产品
 * Created by xugang on 2017/7/29.
 */
public enum GroupEnum {
    common("0", "通用"), user("1", "会员"), trade("2", "交易"), product("3", "产品"), unknown("-1", "unknown");

    private String code;
    private String desc;

    GroupEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static GroupEnum findByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return unknown;
        }
        for (GroupEnum item : GroupEnum.values()) {
            if (item.getCode().equalsIgnoreCase(code)) {
                return item;
            }
        }
        return unknown;
    }
}
