package com.invest.ivuser.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivuser.model.entity.UserPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class UserPolicyManager extends BaseManager {

    public UserPolicy getUserPolicyById(Long userPolicyId) {
        UserPolicy result;
        try {
            result = userPolicyDAO.selectByPrimaryKey(userPolicyId);
        } catch (Exception e) {
            logger.error("查询失败,userPolicyId=" + userPolicyId, e);
            throw new IVDAOException(e);
        }
        return result;
    }

    public void batchSaveUserPolicy(List<UserPolicy> userPolicyList) {
        try {
            userPolicyDAO.batchInsert(userPolicyList);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
    }

    public void updateUserPolicyById(UserPolicy userPolicy) {
        try {
            int line = userPolicyDAO.updateByPrimaryKey(userPolicy);
            logger.info("保存修改用户策略成功,line={},userPolicy={}", line, JSONObject.toJSONString(userPolicy));
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
    }

    //逻辑删除
    public void deleteBatchUserPolicyById(List<Long> userPolicyIds) {
        try {
            userPolicyDAO.batchDeleteByPrimaryKeys(userPolicyIds);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
    }

    public List<UserPolicy> getUserPolicyList(UserPolicy userPolicy) {
        List<UserPolicy> result;
        try {
            result = userPolicyDAO.selectListBySelective(userPolicy);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return result;
    }

    public boolean existsUserPolicy(Long userId, Long policyId) {
        try {
            Long id = userPolicyDAO.selectExistsUserPolicyJoin(userId, policyId, null);
            if (id != null) return true;
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return false;
    }

    public int getUserPolicyCount(Long userId, Short policyType, Short status) {
        int count;
        try {
            count = userPolicyDAO.selectUserPolicyCountJoin(userId, policyType, status);
        } catch (Exception e) {
            logger.error("查询散标策略总数失败,userId=" + userId, e);
            throw new IVDAOException(e);
        }
        return count;
    }

    public boolean existsUserPolicy(Long userId, Long policyId, Short status) {
        try {
            Long id = userPolicyDAO.selectExistsUserPolicyJoin(userId, policyId, status);
            if (id != null) return true;
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return false;
    }

    public List<UserPolicy> getNoSelPolicyThirdUUIDList(Long userId, Short policyType, Long mainPolicyId) {
        List<UserPolicy> result;
        try {
            result = userPolicyDAO.selectThirdMainSubPolicyList(userId, policyType, mainPolicyId);
        } catch (Exception e) {
            logger.error("查询用户第三方自定义散标策略失败,userId=" + userId + ",policyType=" + policyType + ",policyType=" + policyType, e);
            throw new IVDAOException(e);
        }
        return result;
    }
}
