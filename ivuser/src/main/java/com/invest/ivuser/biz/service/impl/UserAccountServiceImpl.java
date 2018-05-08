package com.invest.ivuser.biz.service.impl;

import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivuser.biz.manager.UserAccountManager;
import com.invest.ivuser.biz.service.UserAccountService;
import com.invest.ivuser.common.IVUserErrorEnum;
import com.invest.ivuser.model.entity.UserAccount;
import com.invest.ivuser.model.result.UserAccountResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xugang on 2017/7/28.
 */
@Service
public class UserAccountServiceImpl implements UserAccountService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserAccountManager userAccountManager;

    @Override
    public UserAccountResult getUserAccount(Long userId) {
        UserAccountResult result = new UserAccountResult();
        try {
            UserAccount userAccount = userAccountManager.getUserAccountByUserId(userId);
            if (userAccount == null) {
                IVUserErrorEnum.QUERY_USER_ACCOUNT_IS_NULL_ERROR.fillResult(result);
                return result;
            }
            result.setUserAccount(userAccount);
        } catch (Exception e) {
            IVUserErrorEnum.QUERY_USER_ACCOUNT_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public UserAccountResult addUserAccountBalance(Long userId, Integer buyCount) {
        if (userId == null || buyCount == null) {
            throw new IllegalArgumentException("添加账户捉宝币失败,参数错误");
        }
        UserAccountResult result = new UserAccountResult();
        UserAccount userAccount = new UserAccount();
        userAccount.setUpdateTime(DateUtil.getCurrentDatetime());
        userAccount.setUserId(userId);
        userAccount.setBidAmountBalance(buyCount * UserAccount.ZHUOBAOBI_BID_AMOUNT_BASE); //增加的可投金额
        try {
            userAccountManager.updateUserAccountBalance(userAccount);
        } catch (Exception e) {
            IVUserErrorEnum.UPDATE_USER_ACCOUNT_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public UserAccountResult withholdUserAccountBalance(Long userId, Integer amount) {
        if (userId == null || amount == null) {
            throw new IllegalArgumentException("扣款账户捉宝币失败,参数错误");
        }
        UserAccountResult result = new UserAccountResult();
        UserAccount userAccount = new UserAccount();
        userAccount.setUpdateTime(DateUtil.getCurrentDatetime());
        userAccount.setUserId(userId);
        userAccount.setBidAmountBalance(-amount); //减少的可投金额
        try {
            userAccountManager.updateUserAccountBalance(userAccount);
        } catch (Exception e) {
            IVUserErrorEnum.UPDATE_USER_ACCOUNT_ERROR.fillResult(result);
        }
        return result;
    }
}
