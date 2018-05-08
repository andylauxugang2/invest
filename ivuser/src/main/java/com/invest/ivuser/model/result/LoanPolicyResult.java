package com.invest.ivuser.model.result;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivuser.model.entity.LoanPolicy;
import com.invest.ivuser.model.entity.UserLoanRecord;
import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class LoanPolicyResult extends Result {
    private static final long serialVersionUID = -6209450697963255965L;
    private List<LoanPolicy> loanPolicies;
    private LoanPolicy loanPolicy;
}
