package com.invest.ivcommons.core;

import com.invest.ivcommons.configcenter.PropertiesUtil;

/**
 * Created by xugang on 2017/7/29.
 */
public class AppProfile {

    private static final String profileFile = "ivcommons.properties";
    private static final String appNameKey = "ivbase.appName";
    private static final String envKey = "ivbase.environment";

    static String appName; //唯一应用标识
    static String environment; //各套环境

    static {
        PropertiesUtil.init(profileFile);
    }

    /**
     * 获取客户端配置的appName
     *
     * @return
     */
    public static String getAppName() {
        return PropertiesUtil.getProperty(appNameKey);
    }

    /**
     * 获取客户端环境 开发\测试\预发\正式
     *
     * @return
     */
    public static String getEnvironment() {
        return PropertiesUtil.getProperty(envKey);
    }

}
