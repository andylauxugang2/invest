package com.invest.ivuser.biz.service;

import com.invest.ivuser.model.result.UserAccountResult;

/**
 * Created by xugang on 2017/7/28.
 */
public interface UserAccountService {

    UserAccountResult getUserAccount(Long userId);

    /**
     * 修改捉宝币金额 db ACID保证并发一致性
     * @param userId
     * @param buyCount 增加的捉宝币
     * @return
     */
    UserAccountResult addUserAccountBalance(Long userId, Integer buyCount);

    /**
     * 投标扣款 sql计算扣款捉宝币
     * @param userId
     * @param amount 投标金额
     * @return
     */
    UserAccountResult withholdUserAccountBalance(Long userId, Integer amount);
}
