package com.invest.ivdata.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class SchoolType extends BaseEntity {

    private static final long serialVersionUID = 6367748420258528997L;

    public static final short TYPE_985 = 5;
    public static final short TYPE_211 = 4;
    public static final short TYPE_1BEN = 3;
    public static final short TYPE_2BEN = 2;
    public static final short TYPE_3BEN = 1;

    private String name;
    private Short type;
    private Short subType;

}
