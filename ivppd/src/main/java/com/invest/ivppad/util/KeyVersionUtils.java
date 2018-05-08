package com.invest.ivppad.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注意 : default-lazy-init="false" 否则注入的时候才会用
 * Created by xugang on 2017/8/2.
 */
public class KeyVersionUtils implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(KeyVersionUtils.class);

    private static final String KEY_PREFIX_ACCESSTOKEN_V = "v1_";
    private static final String KEY_PREFIX_PPD_LOAN_DETAIL_V = "v1_";
    private static final String KEY_PREFIX_PPD_USER_ACCOUNT_BALANCE_V = "v1_";

    private static final String VALUE_STRING_BLANK = "";
    private static final String VALUE_BOOLEAN_TRUE = "true";
    private static final String VALUE_BOOLEAN_FALSE = "false";

    private enum ConfigData {
        ppdAccessTokenV("rediskey.v.ppd.accesstoken"),
        loanDetailListV("rediskey.v.ppd.loandetaillist"),
        ppdUserAccountBalanceV("rediskey.v.ppd.useraccountbalance"),
        ppdDebug("ppd.debug");

        private String key;

        public String getKey() {
            return key;
        }

        ConfigData(String key) {
            this.key = key;
        }
    }

    static {
        /*com.ly.tcbase.core.log.ComponentLog.setLoggerLevel(org.apache.logging.log4j.Level.ERROR);
        ConfigCenterClient.init();
        ConfigCenterClient.onChangeChanged((data) -> {
            updateConfig();
        });*/
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        updateConfig();
    }


    private static Map<String, String> data = new ConcurrentHashMap<>();

    private static void updateConfig() {
//        data.put(ConfigData.ssimV.getKey(), get(ConfigData.ssimV.getKey(), VALUE_STRING_BLANK));
        String k = ConfigData.ppdAccessTokenV.getKey();
        String v = KEY_PREFIX_ACCESSTOKEN_V;
        logger.info("设置配置变量:key={},v={}", k, v);
        data.put(k, v);

        k = ConfigData.loanDetailListV.getKey();
        v = KEY_PREFIX_PPD_LOAN_DETAIL_V;
        logger.info("设置配置变量:key={},v={}", k, v);
        data.put(k, v);

        k = ConfigData.ppdUserAccountBalanceV.getKey();
        v = KEY_PREFIX_PPD_USER_ACCOUNT_BALANCE_V;
        logger.info("设置配置变量:key={},v={}", k, v);
        data.put(k, v);
    }

    private static String get(String name, String defaultValue) {
        /*String value = null;
        try {
            value = ConfigCenterClient.get(name);
            if (StringUtils.isNotBlank(value)) {
                value = value.trim();
            }
        } catch (Exception e) {
            logger.error("ConfigCenter read failed | name=" + name, ExceptionUtils.getFullStackTrace(e));
        }
        String result = StringUtils.isNotEmpty(value) ? value : defaultValue;
        logger.error("配置:{},更新后数据为:{}", name, result);
        return result;*/
        return null;

    }

    private static String getData(String name) {
        //本地缓存
        return data.get(name);
    }

    public static String rediskeyPpdAccessTokenV(String key) {
        if (StringUtils.isBlank(key)) {
            return key;
        }
        return getData(ConfigData.ppdAccessTokenV.getKey()) + key.trim();
    }

    public static String rediskeyLoanDetailListV(String key) {
        if (StringUtils.isBlank(key)) {
            return key;
        }
        return getData(ConfigData.loanDetailListV.getKey()) + key.trim();
    }

    public static String rediskeyUserAccountBalanceV(String key) {
        if (StringUtils.isBlank(key)) {
            return key;
        }
        return getData(ConfigData.ppdUserAccountBalanceV.getKey()) + key.trim();
    }

    /**
     * 是否 ifoag 开启 debug 模式
     *
     * @return
     */
    public static boolean isPPDDebug() {
        String v = getData(ConfigData.ppdDebug.getKey());
        if (StringUtils.isBlank(v)) {
            return false;
        }
        if (VALUE_BOOLEAN_TRUE.equals(v)) {
            return true;
        }
        if (VALUE_BOOLEAN_FALSE.equals(v)) {
            return false;
        }
        return false;
    }
}
