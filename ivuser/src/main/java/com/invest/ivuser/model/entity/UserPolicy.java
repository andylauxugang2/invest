package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserPolicy extends BaseEntity {

    public static final short STATUS_ON = 1;
    public static final short STATUS_OFF = 0;

    private static final long serialVersionUID = 1732693107807299285L;

    public static final int BIDAMOUNT_DEFAULT = 50;
    private Long userId;
    private Short status; //策略状态 1-开启,0-停止
    private String thirdUserUUID;
    private Long policyId;
    private Integer bidAmount;
}
