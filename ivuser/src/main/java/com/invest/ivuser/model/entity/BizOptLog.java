package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class BizOptLog extends BaseEntity {

    private static final long serialVersionUID = 8716060772064975335L;

    public static final short OPT_TYPE_ADD = 1;
    public static final short OPT_TYPE_UPD = 2;
    public static final short OPT_TYPE_DEL = 3;

    public static final short STATUS_LOAN_POLICY_NOSYNC = 0;
    public static final short STATUS_LOAN_POLICY_SYNC = 1;

    private Long userId;
    private Long bizId;
    private Short optType;
    private Short status; //业务状态,比如,散标策略日志,0-未同步过h2,2-同步过h2
}
