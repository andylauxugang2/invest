package com.invest.ivuser.model.result;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivuser.model.entity.LoanPolicy;
import com.invest.ivuser.model.entity.UserPolicy;
import com.invest.ivuser.model.entity.ext.UserPolicyDetail;
import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserPolicyResult extends Result {
    private static final long serialVersionUID = -4619288839107627161L;

    private List<LoanPolicy> loanPolicies;

    private LoanPolicy loanPolicy;

    private List<UserPolicy> userPolicies;

    private List<UserPolicyDetail> userPolicyDetails;

    private int userPolicyCount;
}
