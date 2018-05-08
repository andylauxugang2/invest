package com.invest.ivmanager.biz.service;

import com.invest.ivmanager.model.ListRange;
import com.invest.ivmanager.model.request.LoanPolicyReq;
import com.invest.ivmanager.model.request.SaveLoanPolicyReq;
import com.invest.ivmanager.model.vo.LoanPolicyVO;
import com.invest.ivuser.model.entity.LoanPolicy;

import java.util.List;

/**
 * Created by xugang on 2017/10/12.do best.
 */
public interface LoanPolicyService {

    ListRange getLoanPolicies(LoanPolicyReq loanPolicyReq);

    LoanPolicyVO findLoanPolicyDetailById(Long policyId);

    void dropLoanPolicyById(Long policyId);

    void saveLoanPolicy(SaveLoanPolicyReq saveLoanPolicyReq);

    void updateLoanPolicyStatusByIds(Long userId, List<Long> ids, Short status);
}
