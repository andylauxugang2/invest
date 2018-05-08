package com.invest.ivpush.message.client.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * 模板所在group内所属业务code，0-通用，1-产品组-ppd
 * Created by xugang on 2017/7/29.
 */
public enum CodeEnum {
    common("0", "通用"), product_ppd("1", "产品PPD"), unknown("-1", "unknown");

    private String code;
    private String desc;

    CodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static CodeEnum findByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return unknown;
        }
        for (CodeEnum item : CodeEnum.values()) {
            if (item.getCode().equalsIgnoreCase(code)) {
                return item;
            }
        }
        return unknown;
    }
}
