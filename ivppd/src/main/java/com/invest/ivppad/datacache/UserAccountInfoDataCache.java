package com.invest.ivppad.datacache;

import com.invest.ivcommons.localcache.guava.AbstractLocalCache;
import com.invest.ivcommons.redis.client.cacheclient.CacheClientHA;
import com.invest.ivppad.biz.service.ppdopenapi.AccountService;
import com.invest.ivppad.model.result.PPDOpenApiAccountResult;
import com.invest.ivppad.util.KeyVersionUtils;
import com.invest.ivppad.util.RedisKeyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ppd账户
 * 如果用户充值,靠job去刷新redis 余额
 * Created by xugang on 17/1/8.
 */
@Component
public class UserAccountInfoDataCache extends AbstractLocalCache<String, Integer> {
    private static final int DEFAULT_SECONDS = 60 * 2;

    @Value("${refreshUserAccountInfoInSec}")
    private Integer refreshUserAccountInfoInSec;

    @Resource(name = "ppdCacheClientHA")
    protected CacheClientHA ppdCacheClientHA;

    @Resource
    protected AccountService accountService;

    @Override
    public Integer load(String username) throws Exception {
        String key = KeyVersionUtils.rediskeyUserAccountBalanceV(RedisKeyUtils.keyUserAccountBalance(username));
        String value = ppdCacheClientHA.String().get(key);
        //redis key 过期后 重新查询加入缓存 放入redis
        if (StringUtils.isEmpty(value)) {
            logger.warn("获取redis账户余额为空,username={}", username);
            //查询账户余额
            PPDOpenApiAccountResult accountResult = accountService.getAccountBalance(username);
            if (accountResult.isFailed()) {
                logger.error("获取用户账户余额失败,userName={},error={}", username, accountResult);
                //修改本地缓存余额为-1 标识异常
                return -1;
            }
            int balance = accountResult.getBalance(); //分

            //放入redis
            try {
                ppdCacheClientHA.String().setex(key, RedisKeyUtils.KEY_EXPIRETIME_USER_ACCOUNT_BALANCE, String.valueOf(balance));
            } catch (Exception e) {
                logger.error("放入redis余额异常,username=" + username, e);
            }
            return balance;
        }
        Integer balance = Integer.valueOf(value);
        logger.info("加载用户账户余额到本地缓存成功,balance={}[分],username={}", balance, username);
        return balance;
    }

    @Override
    public int getMaxSize() {
        return 100000;
    }

    @Override
    public int getRefreshInSeconds() {
        if (this.refreshUserAccountInfoInSec == null) {
            return DEFAULT_SECONDS;
        }
        return refreshUserAccountInfoInSec;
    }

    public void updateUserAccountBalance(String username, Integer balance) {
        if (StringUtils.isEmpty(username) || balance == null) {
            logger.error("输入参数不能为空");
            return;
        }
        //更新redis 保存分单位
        try {
            Integer value = get(username) - balance;
            String key = KeyVersionUtils.rediskeyUserAccountBalanceV(RedisKeyUtils.keyUserAccountBalance(username));
            ppdCacheClientHA.String().setex(key, RedisKeyUtils.KEY_EXPIRETIME_USER_ACCOUNT_BALANCE, String.valueOf(value));
            //实时更新本地缓存
            put(username, value);
        } catch (Exception e) {
            logger.error("更新redis余额异常,username=" + username, e);
        }

    }


}
