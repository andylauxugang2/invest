package com.invest.ivppad.biz.service.ppdopenapi;

import com.invest.ivppad.model.result.PPDOpenApiAccountResult;

/**
 * 用户账户
 * Created by xugang on 2017/8/1.
 */
public interface AccountService {

    /**
     * 获取用户账户余额
     * @param userName
     * @return
     */
    PPDOpenApiAccountResult getAccountBalance(String userName);
}
