package com.invest.ivuser.model.param;

import lombok.Data;

/**
 * 用户散标策略参数定义
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserPolicyParam extends UserBaseParam {
    private Short policyType;
    private Short riskLevel;
    private Short status; //策略开始状态
    private Long policyId;
}
