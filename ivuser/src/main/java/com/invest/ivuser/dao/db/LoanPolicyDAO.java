package com.invest.ivuser.dao.db;

import com.invest.ivcommons.dal.base.BaseDAO;
import com.invest.ivuser.model.entity.LoanPolicy;
import com.invest.ivuser.model.entity.ext.UserPolicyDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanPolicyDAO extends BaseDAO<LoanPolicy> {

    void batchDeleteByPrimaryKeys(List<Long> loanPolicyIds);

    void batchUpdateDeletedByPrimaryKeys(List<Long> loanPolicyIds);

    //ext
    List<UserPolicyDetail> selectUserPolicyLeftJoinLoanPolicy(@Param(value = "userId") Long userId,
                                                              @Param(value = "thirdUserUUID") String thirdUserUUID,
                                                              @Param(value = "policyType") Short policyType,
                                                              @Param(value = "riskLevel") Short riskLevel,
                                                              @Param(value = "status") Short status,
                                                              @Param(value = "policyId") Long policyId);

    int updateCommonByPrimaryKey(LoanPolicy loanPolicy);
}