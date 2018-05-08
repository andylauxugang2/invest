package com.invest.ivppad.common;

import com.invest.ivdata.model.entity.SchoolType;

/**
 * 学校类型
 * Created by xugang on 2016/11/1.
 */
public enum SchoolTypeEnum {

    type985(PPDBinExpConstants.B28, "985", SchoolType.TYPE_985),
    type211(PPDBinExpConstants.B29, "211", SchoolType.TYPE_211),
    type1ben(PPDBinExpConstants.B30, "一本", SchoolType.TYPE_1BEN),
    type2ben(PPDBinExpConstants.B31, "二本", SchoolType.TYPE_2BEN),
    type3ben(PPDBinExpConstants.B32, "三本", SchoolType.TYPE_3BEN),
    typezhigao(PPDBinExpConstants.B33, "职高", (short) -1);

    private long code;
    private String name;
    private Short type;

    SchoolTypeEnum(long code, String name, Short type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Short getType() {
        return type;
    }

    public static SchoolTypeEnum findByCode(long code) {
        for (SchoolTypeEnum item : SchoolTypeEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }

    public static SchoolTypeEnum findByType(Short type) {
        for (SchoolTypeEnum item : SchoolTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }
}
