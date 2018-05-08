package com.invest.ivuser.model.param;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class LoanPolicyParam extends UserBaseParam {
    private Short riskLevel; //风险等级 @see LoanRiskLevelEnum
    private Short policyType;
    private Boolean isDelete;
    private Short status;
}
