package com.invest.ivuser.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivuser.model.entity.UserMainPolicy;
import com.invest.ivuser.model.vo.UserMainPolicyVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class UserMainPolicyManager extends BaseManager {


    public List<UserMainPolicyVO> getUserMainPolicyList(UserMainPolicy userMainPolicy) {
        List<UserMainPolicyVO> result;
        try {
            result = userMainPolicyDAO.selectUserMainPolicy(userMainPolicy);
        } catch (Exception e) {
            logger.error("查询用户主策略失败,userMainPolicy=" + JSONObject.toJSONString(userMainPolicy), e);
            throw new IVDAOException(e);
        }
        return result;
    }

    public List<UserMainPolicy> getUserMainPolicyEntityList(UserMainPolicy userMainPolicy) {
        List<UserMainPolicy> result;
        try {
            result = userMainPolicyDAO.selectListBySelective(userMainPolicy);
        } catch (Exception e) {
            logger.error("查询用户主策略失败,userMainPolicy=" + JSONObject.toJSONString(userMainPolicy), e);
            throw new IVDAOException(e);
        }
        return result;
    }

    public UserMainPolicyVO getUserMainPolicyByUniqueKey(Long userId, String thirdUserUUID, Long mainPolicyId) {
        UserMainPolicyVO result;
        try {
            result = userMainPolicyDAO.selectUserMainPolicyByUniqueKey(userId, thirdUserUUID, mainPolicyId);
        } catch (Exception e) {
            logger.error("查询用户主策略失败,userId=" + userId + ",thirdUserUUID=" + thirdUserUUID + ",mainPolicyId=" + mainPolicyId, e);
            throw new IVDAOException(e);
        }
        //logger.info("查询用户主策略成功,result={}", JSONObject.toJSONString(result));
        return result;
    }

    public void saveUserMainPolicy(UserMainPolicy userMainPolicy) {
        Long id = userMainPolicy.getId();
        try {
            if (id == null) {
                int line = userMainPolicyDAO.insert(userMainPolicy);
                if (line == 1) {
                    logger.info("插入用户主策略策略成功,userMainPolicy={}", JSONObject.toJSONString(userMainPolicy));
                } else {
                    throw new IVDAOException("插入用户主策略数据库返回行数不为1");
                }
            } else {
                userMainPolicy.setCreateTime(null);
                userMainPolicyDAO.updateByPrimaryKey(userMainPolicy);
            }

        } catch (Exception e) {
            logger.error("插入用户主策略失败,userMainPolicy=" + JSONObject.toJSONString(userMainPolicy), e);
            throw new IVDAOException(e);
        }
    }

    public void delUserMainPolicy(Long userId, String thirdUserUUID, Long mainPolicyId) {
        try {
            userMainPolicyDAO.deleteBySelective(userId, thirdUserUUID, mainPolicyId);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
    }
}
