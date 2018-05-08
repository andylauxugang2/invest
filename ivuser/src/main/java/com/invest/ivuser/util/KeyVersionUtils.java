package com.invest.ivuser.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注意 : default-lazy-init="false" 否则注入的时候才会用
 * <context:component-scan base-package="com.invest.ivuser" /> 扫不到
 * <bean class="com.invest.ivuser.util.KeyVersionUtils"/> 可以加载
 * Created by xugang on 2017/8/2.
 */
public class KeyVersionUtils implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(KeyVersionUtils.class);

    private static final String KEY_PREFIX_SMS_CHECK_CODE_V = "v1_";
    private static final String KEY_PREFIX_BLACKLIST_THIRD_V = "v1_";
    private static final int SMS_CHECK_CODE_EXPIRETIME = 60 * 5; //5分钟

    private static final String VALUE_STRING_BLANK = "";
    private static final String VALUE_BOOLEAN_TRUE = "true";
    private static final String VALUE_BOOLEAN_FALSE = "false";

    private enum ConfigData {
        userSMSCheckCodeV("rediskey.v.user.smscheckcode"),
        userSMSCheckCodeExpiretime("user.smscheckcode.expiretime"),
        blackListThirdV("rediskey.v.blacklist.third");

        private String key;

        public String getKey() {
            return key;
        }

        ConfigData(String key) {
            this.key = key;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        updateConfig();
    }


    private static Map<String, String> data = new ConcurrentHashMap<>();

    private static void updateConfig() {
//        data.put(ConfigData.ssimV.getKey(), get(ConfigData.ssimV.getKey(), VALUE_STRING_BLANK));
        String k = ConfigData.userSMSCheckCodeV.getKey();
        String v = KEY_PREFIX_SMS_CHECK_CODE_V;
        logger.info("设置配置变量:key={},v={}", k, v);
        data.put(k, v);

        k = ConfigData.userSMSCheckCodeExpiretime.getKey();
        v = String.valueOf(SMS_CHECK_CODE_EXPIRETIME);
        logger.info("设置配置变量:key={},v={}", k, v);
        data.put(k, v);

        k = ConfigData.blackListThirdV.getKey();
        v = String.valueOf(KEY_PREFIX_BLACKLIST_THIRD_V);
        logger.info("设置配置变量:key={},v={}", k, v);
        data.put(k, v);
    }


    private static String getData(String name) {
        //本地缓存
        return data.get(name);
    }

    public static String rediskeyUserSMSCheckCodeV(String key) {
        if (StringUtils.isBlank(key)) {
            return key;
        }
        return getData(ConfigData.userSMSCheckCodeV.getKey()) + key.trim();
    }

    public static long getUserSMSCheckCodeExpiretime() {
        return Long.valueOf(getData(ConfigData.userSMSCheckCodeExpiretime.getKey()));
    }

    public static String rediskeyBlackListThirdV(String key) {
        if (StringUtils.isBlank(key)) {
            return key;
        }
        return getData(ConfigData.blackListThirdV.getKey()) + key.trim();
    }
}
