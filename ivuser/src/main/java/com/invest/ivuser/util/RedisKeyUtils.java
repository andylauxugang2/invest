package com.invest.ivuser.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xugang on 2017/8/2.
 */
public final class RedisKeyUtils {
    private static final Logger logger = LoggerFactory.getLogger(RedisKeyUtils.class);

    private static final String KEY_PREFIX_USER_SMS_CODE = "smscode";
    private static final String KEY_PREFIX_USERTOKEN = "usertoken";
    private static final String KEY_PREFIX_BLACKLISTTHIRD = "blacklistthird";

    public static String keyUserSMSCode(String phone) {
        return KEY_PREFIX_USER_SMS_CODE + ":" + phone;
    }

    public static String keyUserToken(String token) {
        return KEY_PREFIX_USERTOKEN + ":" + token;
    }

    public static String keyBlackListThird(String third) {
        return KEY_PREFIX_BLACKLISTTHIRD + ":" + third;
    }
}
