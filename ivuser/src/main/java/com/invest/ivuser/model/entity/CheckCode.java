package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class CheckCode extends BaseEntity {
    private static final long serialVersionUID = 6193848291526673406L;
    private String mobile;
    private String checkCode;
    private Integer type; //验证码类型，@see CheckCodeTypeEnum
    private Integer expireTime; //过期时间
}
