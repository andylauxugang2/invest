package com.invest.ivuser.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivuser.model.entity.UserThirdBindInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class UserThirdBindInfoManager extends BaseManager {
    /**
     * 根据 Id 查询
     */
    public UserThirdBindInfo getById(Long id) {
        UserThirdBindInfo result;
        try {
            result = userThirdBindInfoDAO.selectByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("查询第三方信息失败,id=" + id, e);
            throw new IVDAOException(e);
        }
        return result;
    }

    /**
     * 根据userId 查询
     */
    public List<UserThirdBindInfo> getByUserId(Long userId) {
        List<UserThirdBindInfo> result;
        try {
            UserThirdBindInfo param = new UserThirdBindInfo();
            param.setUserId(userId);
            result = userThirdBindInfoDAO.selectListBySelective(param);
        } catch (Exception e) {
            logger.error("查询第三方信息失败,userId=" + userId, e);
            throw new IVDAOException(e);
        }
        //logger.info("查询第三方信息成功,userId={},result={}", userId, JSONObject.toJSONString(result));
        return result;
    }

    /**
     * 根据userId 查询
     */
    public List<UserThirdBindInfo> getByUserIdAndThirdUserUUID(Long userId, String thirdUserUUID) {
        List<UserThirdBindInfo> result;
        try {
            UserThirdBindInfo param = new UserThirdBindInfo();
            param.setUserId(userId);
            param.setThirdUserUUID(thirdUserUUID);
            result = userThirdBindInfoDAO.selectListBySelective(param);
        } catch (Exception e) {
            logger.error("查询第三方信息失败,userId=" + userId, e);
            throw new IVDAOException(e);
        }
        //logger.info("查询第三方信息成功,userId={},result={}", userId, JSONObject.toJSONString(result));
        return result;
    }

    /**
     * 根据userId 查询
     */
    public List<UserThirdBindInfo> getList(UserThirdBindInfo userThirdBindInfo) {
        List<UserThirdBindInfo> result;
        try {
            result = userThirdBindInfoDAO.selectListBySelective(userThirdBindInfo);
        } catch (Exception e) {
            logger.error("查询第三方信息失败,userThirdBindInfo=" + JSONObject.toJSONString(userThirdBindInfo), e);
            throw new IVDAOException(e);
        }
        //logger.info("查询第三方信息成功,userId={},result={}", userId, JSONObject.toJSONString(result));
        return result;
    }

    /**
     * 插入
     */
    public void saveOne(UserThirdBindInfo userThirdBindInfo) {
        try {
            int line = userThirdBindInfoDAO.insert(userThirdBindInfo);
            if (line == 1) {
                logger.info("插入第三方信息成功,userId={},userThirdBindInfo={}", userThirdBindInfo.getThirdUserUUID(), JSONObject.toJSONString(userThirdBindInfo));
            } else {
                throw new IVDAOException("插入第三方信息数据库返回行数不为1");
            }
        } catch (Exception e) {
            logger.error("插入第三方信息失败,userId=" + userThirdBindInfo.getThirdUserUUID(), e);
            throw new IVDAOException(e);
        }
    }

    /**
     * update
     */
    public void updateOneByUserIdAndThirdUserUUID(UserThirdBindInfo userThirdBindInfo) {
        try {
            int line = userThirdBindInfoDAO.updateByUserIdAndThirdUserUUID(userThirdBindInfo);
            if (line == 1) {
                logger.info("更新第三方信息成功,userId={},userThirdBindInfo={}", userThirdBindInfo.getThirdUserUUID(), JSONObject.toJSONString(userThirdBindInfo));
            } else {
                throw new IVDAOException("更新第三方信息数据库返回行数不为1");
            }
        } catch (Exception e) {
            logger.error("更新第三方信息失败,userId=" + userThirdBindInfo.getThirdUserUUID(), e);
            throw new IVDAOException(e);
        }
    }

    public void updateById(UserThirdBindInfo userThirdBindInfo) {
        try {
            int line = userThirdBindInfoDAO.updateByPrimaryKeySelective(userThirdBindInfo);
            if (line == 1) {
                logger.info("更新第三方信息成功,id={},userThirdBindInfo={}", userThirdBindInfo.getId(), JSONObject.toJSONString(userThirdBindInfo));
            } else {
                throw new IVDAOException("更新第三方信息数据库返回行数不为1");
            }
        } catch (Exception e) {
            logger.error("更新第三方信息失败,id=" + userThirdBindInfo.getId(), e);
            throw new IVDAOException(e);
        }
    }

    public void delById(Long bindId) {
        try {
            userThirdBindInfoDAO.deleteByPrimaryKey(bindId);
        } catch (Exception e) {
            logger.error("删除第三方信息失败,bindId=" + bindId, e);
            throw new IVDAOException(e);
        }
    }
}
