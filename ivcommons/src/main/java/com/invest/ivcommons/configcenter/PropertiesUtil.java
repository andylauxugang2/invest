package com.invest.ivcommons.configcenter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by xugang on 2017/7/29.
 */
public final class PropertiesUtil {
    private static Map<String, String> properties = new HashMap<>(10);

    /**
     * 初始化属性文件 /test.properties
     *
     * @param propertiesFilePath
     */
    public static void init(String propertiesFilePath) {
        Properties prop = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFilePath);
        try {
            prop.load(in);
            Set<Object> keys = prop.keySet();//返回属性key的集合
            for (Object key : keys) {
                properties.put(key.toString(), prop.getProperty((String) key).trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key){
        return properties.get(key);
    }

}
