package com.invest.ivcommons.util.format;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by xugang on 2017/3/23.
 */
@Slf4j
public class JSONUtil {

    public static int safeGetInt(JSONObject jsonObject, String key, int defaultValue) {

        Integer value;
        String valueStr = null;
        try {
            if (!jsonObject.containsKey(key)) {
                log.warn("no found key {}, use default value!", key);
                value = defaultValue;
            } else {
                valueStr = jsonObject.getString(key);
                value = Integer.parseInt(valueStr.trim());
                log.debug("------the value of " + key + " is------ " + value);
            }
        } catch (Throwable t) {
            log.debug("get value error use default value!key:{},value:{}", key, defaultValue);
            value = defaultValue;
        }
        return value;
    }

    public static String safeGetString(JSONObject jsonObject, String key, String defaultValue) {

        String value = null;
        try {
            if (!jsonObject.containsKey(key)) {
                log.warn("no found key {}, use default value!", key);
                value = defaultValue;
            } else {
                value = jsonObject.getString(key);
            }
        } catch (Throwable t) {
            log.debug("get value error use default value!key:{},value:{}", key, defaultValue);
            value = defaultValue;
        }
        return value;
    }
}
