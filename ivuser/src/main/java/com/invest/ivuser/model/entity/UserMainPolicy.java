package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

/**
 * 主策略id 必须定死 == 子策略表 policyType
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserMainPolicy extends BaseEntity {

    public static final short STATUS_ONLINE = 1;
    public static final short STATUS_OFFLINE = 0;

    private static final long serialVersionUID = 7344633111550372078L;
    private Long mainPolicyId;
    private Short status = 1;
    private String thirdUserUUID;
    private Long userId;
    private Integer amountStart;
    private Integer amountMax;
    private Integer accountRemain;

}
