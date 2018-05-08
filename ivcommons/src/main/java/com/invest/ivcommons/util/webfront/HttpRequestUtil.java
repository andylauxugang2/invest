package com.invest.ivcommons.util.webfront;

import com.invest.ivcommons.constant.http.HttpConstants;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by xugang on 2017/7/28.
 */
public final class HttpRequestUtil {

    /**
     * 获取客户端IP
     *
     * @param request
     * @return String
     */
    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader(HttpConstants.HEADER_CLIENT_IP);
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isNotEmpty(ip)) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 获取客户端平台
     *
     * @param request
     * @return
     */
    public static String getClientPlat(HttpServletRequest request) {
        String platform = request.getHeader(HttpConstants.HEADER_CLIENT_PLATFORM);
        return platform;
    }

    /**
     * 从request中获得参数Map，并返回可读的Map
     *
     * @param request
     * @return
     */
    public static Map getParameterMap(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

}
