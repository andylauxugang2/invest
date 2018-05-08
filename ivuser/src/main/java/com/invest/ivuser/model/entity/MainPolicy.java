package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class MainPolicy extends BaseEntity {
    private static final long serialVersionUID = 7452287252716909568L;

    public static final long MAIN_POLICY_ID_SYS_LOAN = 1; //系统散标
    public static final long MAIN_POLICY_ID_USER_LOAN = 2; //自定义散标
    public static final long MAIN_POLICY_ID_USER_DEBT = 3; //自定义债权
    public static final long MAIN_POLICY_ID_TRACERMAN = 4; //跟踪
    private String name;
    private Short status;

    //展示用
    private boolean inUse; //用户是否启用 默认不启用
}
