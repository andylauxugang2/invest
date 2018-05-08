package com.invest.ivppad.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by xugang on 2017/8/2.
 */
public final class RedisKeyUtils {
    private static final Logger logger = LoggerFactory.getLogger(RedisKeyUtils.class);

    private static final String KEY_PREFIX_USERNAME_ACCESSTOKEN = "ppdut";
    private static final String KEY_PREFIX_LOAN_DETAIL_LIST = "ppdldl";
    private static final String KEY_PREFIX_USER_ACCOUNT_BALANCE = "ppduacctbalance";
    private static final String KEY_PREFIX_UNM_GETPPDBALANCEERROR = "unotifymsg";

    //key过期时间
    public static final int KEY_EXPIRETIME_USER_ACCOUNT_BALANCE = 60 * 5; //5分钟
    public static final int KEY_EXPIRETIME_UNM_GETPPDBALANCEERROR = 60 * 30; //30分钟

    public static String keyUserNameAccessToken(String ppdUserName) {
        return KEY_PREFIX_USERNAME_ACCESSTOKEN + ":" + ppdUserName;
    }

    public static String keyLoanDetailList() {
        return KEY_PREFIX_LOAN_DETAIL_LIST;
    }

    public static String keyUserAccountBalance(String ppdUserName) {
        return KEY_PREFIX_USER_ACCOUNT_BALANCE + ":" + ppdUserName;
    }

    public static String keyUserNotifyMessageForGetPPDBalanceError(Long userId, String ppdUserName) {
        return KEY_PREFIX_UNM_GETPPDBALANCEERROR + ":" + userId + ppdUserName;
    }
}
