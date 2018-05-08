package com.invest.ivcommons.redis.client.cacheclient;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by xugang on 2017/7/29.
 */
public class CacheClientConstant {
    public static String version = "Unknown";

    static {
        try (InputStream stream = CacheClientConstant.class.getClassLoader().getResourceAsStream("META-INF/maven/com.ly.tcbase/cache/pom.properties")) {
            Properties prop = new Properties();
            prop.load(stream);

            if (StringUtils.isNotBlank(prop.getProperty("version", null)))
                version = prop.getProperty("version");
        } catch (Throwable ignored) {
        }
    }
}
