package com.invest.ivuser.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivuser.model.entity.UserAccount;
import org.springframework.stereotype.Component;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class UserAccountManager extends BaseManager {

    public UserAccount getUserAccountByUserId(Long userId) {
        UserAccount result;
        try {
            result = userAccountDAO.selectOneByUserId(userId);
        } catch (Exception e) {
            logger.error("查询用户账户失败,userId=" + userId, e);
            throw new IVDAOException(e);
        }
        return result;
    }

    public void addUserAccount(UserAccount userAccount) {
        try {
            int line = userAccountDAO.insert(userAccount);
            if (line == 1) {
                logger.info("插入用户账户成功,userAccount={}", JSONObject.toJSONString(userAccount));
            } else {
                throw new IVDAOException("插入用户账户数据库返回行数不为1");
            }
        } catch (Exception e) {
            logger.error("插入用户账户失败,userAccount=" + JSONObject.toJSONString(userAccount), e);
            throw new IVDAOException(e);
        }
    }

    public void updateUserAccountBalance(UserAccount userAccount) {
        try {
            userAccountDAO.updateBalance(userAccount.getUserId(), userAccount.getBidAmountBalance());
        } catch (Exception e) {
            logger.error("更新用户账户余额失败,userAccount=" + JSONObject.toJSONString(userAccount), e);
            throw new IVDAOException(e);
        }
    }
}
