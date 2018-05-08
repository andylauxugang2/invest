
package com.invest.ivcommons.session.http;

import com.invest.ivcommons.session.utils.HttpConstants;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xugang on 2017/7/29.
 */
@SuppressWarnings("static-access")
public class CookieSerializer {

    public static String cookieName;

    public static String cookiePath = HttpConstants.COOKIE_PATH;

    public static Integer cookieMaxAge = -1;

    public static String domainName;


    public static List<String> readCookieValues(HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        List<String> matchingCookieValues = new ArrayList<String>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    String sessionId = cookie.getValue();
                    matchingCookieValues.add(sessionId);
                }
            }
        }
        return matchingCookieValues;
    }


    public static void writeCookieValue(String cookieValue, HttpServletRequest request, HttpServletResponse response) {

        Cookie sessionCookie = new Cookie(cookieName, cookieValue);
        sessionCookie.setPath(cookiePath);
        sessionCookie.setDomain(domainName);
        if (StringUtils.isBlank(cookieValue)) {
            sessionCookie.setMaxAge(0);//设置cookie失效
        } else {
            sessionCookie.setMaxAge(cookieMaxAge);
        }

        response.addCookie(sessionCookie);
    }


    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public void setCookieName(String cookieName) {
        if (StringUtils.isBlank(cookieName)) {
            throw new IllegalArgumentException("cookieName cannot be null");
        }
        this.cookieName = cookieName;
    }


    public void setCookieMaxAge(int cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }


    public void setDomainName(String domainName) {

        this.domainName = domainName;
    }


}
