package com.invest.ivuser.biz.service;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivuser.model.entity.LoanPolicy;
import com.invest.ivuser.model.entity.UserMainPolicy;
import com.invest.ivuser.model.entity.UserPolicy;
import com.invest.ivuser.model.param.LoanPolicyParam;
import com.invest.ivuser.model.param.UserPolicyParam;
import com.invest.ivuser.model.result.LoanPolicyResult;
import com.invest.ivuser.model.result.UserPolicyResult;
import com.invest.ivuser.model.vo.UserMainPolicyVO;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
public interface UserPolicyService {

    /**
     * 查询散标策略总数
     */
    UserPolicyResult getUserPolicyCount(UserPolicyParam param);

    /**
     * 查询用户所有 散标策略
     */
    UserPolicyResult getLoanPolicy(Long id);

    /**
     * 查询用户主策略
     */
    List<UserMainPolicyVO> getUserMainPolicies(Long userId, String thirdUserUUID);

    /**
     * 查询用户主策略
     */
    List<UserMainPolicy> getUserMainPolicies(UserMainPolicy userMainPolicy);

    /**
     * 查唯一 isDeleted = false
     */
    UserMainPolicyVO getUserMainPolicy(Long userId, String thirdUserUUID, Long mainPolicyId);

    /**
     * 保存用户主策略
     */
    UserMainPolicy saveUserMainPolicy(UserMainPolicy userMainPolicy);

    /**
     * 删除用户主策略
     */
    UserPolicyResult removeUserMainPolicy(Long userId, String thirdUserUUID, Long mainPolicyId);

    /**
     * 查询所有散标策略
     */
    LoanPolicyResult getLoanPolicies(LoanPolicyParam param);

    /**
     * 批量添加用户第三方选择的散标策略
     */
    UserPolicyResult addUserPolicyBatch(Long userId, String thirdUserUUID, List<Long> policyIds);

    /**
     * 保存修改 用户选择的散标策略
     */
    Result modifyUserPolicy(UserPolicy userPolicy);

    /**
     * 删除散标策略 userId是操作人ID
     */
    Result delLoanPolicy(Long userId, Long policyId);

    /**
     * 删除散标策略
     */
    Result delLoanPolicy(Long userId, List<Long> policyIds);

    /**
     * 用户添加散标策略
     */
    LoanPolicyResult saveLoanPolicy(LoanPolicy loanPolicy);

    /**
     * 查询用户为第三方账号设置的所有散标策略
     */
    UserPolicyResult getUserPolicies(UserPolicyParam param);

    /**
     * 查询用户为第三方账号设置的所有散标策略 详情
     */
    UserPolicyResult getUserPolicyDetailList(UserPolicyParam param);

    /**
     * 查询未添加过该策略的第三方账号
     */
    List<String> getNoSelPolicyThirdUUIDList(Long userId, Long policyId, Short policyType, Long mainPolicyId);

    /**
     * 挂载用户第三方自定义散标策略
     */
    UserPolicyResult attachUserThirdLoanPolicy(Long userId, Long policyId, List<String> thirdUUIDList);

    /**
     * 删除用户为第三方账户选择的自定义散标策略
     */
    Result dettachUserThirdLoanPolicy(Long userId, List<Long> userPolicyIds);

    /**
     * 修改散标策略
     * @param loanPolicy
     * @return
     */
    Result modifyLoanPolicy(LoanPolicy loanPolicy);
}
