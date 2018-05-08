package com.invest.ivpay.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xugang on 2017/10/18.do best.
 */
@Data
public class Product extends BaseEntity {

    private static final long serialVersionUID = -4668166799864287085L;

    private String name;
    private String describtion;
}
