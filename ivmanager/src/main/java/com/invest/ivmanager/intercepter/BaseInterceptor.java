package com.invest.ivmanager.intercepter;

import com.google.gson.Gson;
import com.invest.ivcommons.base.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class BaseInterceptor extends HandlerInterceptorAdapter {

    protected Logger logger = LoggerFactory.getLogger("mvcLogger");

    protected void responseError(HttpServletResponse response, Result result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String json = new Gson().toJson(result);
        out.println(json);
        out.close();
    }

    protected String getCookie(HttpServletRequest request, String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie c : cookies) {
            if (name.equalsIgnoreCase(c.getName())) {
                logger.info("getCookie success name={}, value={}", name, c.getValue());
                return c.getValue();
            }
        }
        logger.info("getCookie result is null, name={}", name);
        return null;
    }

}
