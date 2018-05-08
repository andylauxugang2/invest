package com.invest.ivuser.dao.db;

import com.invest.ivcommons.dal.base.BaseDAO;
import com.invest.ivuser.model.entity.UserPolicy;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPolicyDAO extends BaseDAO<UserPolicy> {

    int batchInsert(List<UserPolicy> records);

    void batchDeleteByPrimaryKeys(List<Long> userSysLoanPolicyIds);

    List<UserPolicy> selectThirdMainSubPolicyList(@Param(value = "userId") Long userId,
                                                  @Param(value = "policyType") Short policyType,
                                                  @Param(value = "mainPolicyId") Long mainPolicyId);

    Long selectExistsUserPolicyJoin(@Param(value = "userId") Long userId,
                                    @Param(value = "policyId") Long policyId,
                                    @Param(value = "userPolicyStatus") Short userPolicyStatus);

    int selectUserPolicyCountJoin(@Param(value = "userId") Long userId,
                                  @Param(value = "policyType") Short policyType,
                                  @Param(value = "userPolicyStatus") Short userPolicyStatus);
}