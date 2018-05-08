package com.invest.ivuser.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivcommons.util.security.md5.MD5Util;
import com.invest.ivuser.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class UserManager extends BaseManager {
    /**
     * 根据手机号 查询用户基本信息
     *
     * @param mobile
     * @return
     */
    public User getUserByMobile(String mobile) {
        User result;
        try {
            User user = new User();
            user.setMobile(mobile);
            result = userDAO.selectOneBySelective(user);
        } catch (Exception e) {
            logger.error("查询用户失败,mobile=" + mobile, e);
            throw new IVDAOException(e);
        }
//        logger.info("查询用户成功,mobile={},user={}", mobile, JSONObject.toJSONString(result));
        return result;
    }

    public User getUserById(Long userId) {
        User result;
        try {
            result = userDAO.selectByPrimaryKey(userId);
        } catch (Exception e) {
            logger.error("查询用户失败,userId=" + userId, e);
            throw new IVDAOException(e);
        }
        //logger.info("查询用户成功,userId={},user={}", userId, JSONObject.toJSONString(result));
        return result;
    }


    /**
     * 生成用户token
     *
     * @param user
     * @return
     */
    public String genUserToken(User user) {
        StringBuilder token = new StringBuilder();
        token.append(user.getMobile()).append(System.currentTimeMillis());
        return MD5Util.md5(token.toString());
    }

    /**
     * 插入新用户
     *
     * @param user
     */
    public void saveUser(User user) {
        try {
            int line = userDAO.insert(user);
            if (line == 1) {
                logger.info("插入用户成功,mobile={},user={}", user.getMobile(), JSONObject.toJSONString(user));
            } else {
                throw new IVDAOException("插入用户数据库返回行数不为1");
            }
        } catch (Exception e) {
            logger.error("插入用户失败,mobile=" + user.getMobile(), e);
            throw new IVDAOException(e);
        }
    }

    public void updateUserById(User user) {
        try {
            userDAO.updateByPrimaryKeySelective(user);
        } catch (Exception e) {
            logger.error("更新用户失败,userId=" + user.getId(), e);
            throw new IVDAOException(e);
        }
    }

    public List<User> getUserList(User user) {
        try {
            return userDAO.selectListBySelective(user);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
    }
}
