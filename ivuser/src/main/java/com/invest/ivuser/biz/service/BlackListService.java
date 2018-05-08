package com.invest.ivuser.biz.service;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivuser.model.entity.BlackListThird;
import com.invest.ivuser.model.result.BlackListResult;

/**
 * Created by xugang on 2017/7/28.
 */
public interface BlackListService {

    /**
     * 添加第三方账户黑名单
     */
    Result addBlackListThird(BlackListThird blackListThird);

    /**
     * 查询第三方账户黑名单
     */
    BlackListResult getBlackListThirdList(BlackListThird blackListThird);

    /**
     * 删除黑名单
     * @param type
     * @param value
     * @return
     */
    Result removeBlackList(String type, String value);
}
