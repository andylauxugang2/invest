package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class BlackListThird extends BaseEntity {
    private static final long serialVersionUID = 8562804316273543680L;

    public static final String TYPE_THIRD_USERNAME = "1";

    private String blacklistValue;
    private String blacklistType;

}
