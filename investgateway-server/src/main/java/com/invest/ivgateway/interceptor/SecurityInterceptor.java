package com.invest.ivgateway.interceptor;

import com.alibaba.fastjson.JSON;
import com.invest.ivcommons.security.datacache.ChannelUrlDataCache;
import com.invest.ivcommons.security.domain.ChannelUrl;
import com.invest.ivcommons.session.http.HttpSessionSuport;
import com.invest.ivgateway.constants.HttpConstant;
import com.invest.ivuser.biz.service.UserService;
import com.invest.ivuser.model.entity.User;
import com.invest.ivusergateway.common.constants.CodeEnum;
import com.invest.ivusergateway.common.constants.SessionConstants;
import com.invest.ivusergateway.model.response.APIResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by xugang on 2017/7/28.
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {
    public static Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    @Autowired
    private UserService userService;

    /*@Autowired
    private ChannelUrlDataCache channelUrlDataCache;*/

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestUri = request.getRequestURI();
        String basePath = request.getContextPath();
        String realUri = requestUri.replace(basePath, "");
       /* String source = request.getHeader(HttpConstant.REQUEST_HEADER_SOURCE);
        if(StringUtils.isEmpty(source)) source = request.getParameter(HttpConstant.REQUEST_PARAM_SOURCE);

        logger.info("source = {}", source);
        logger.info("realUri = {}", realUri);
        //1.非法URL
        ChannelUrl channelUrl = channelUrlDataCache.getChannelUrlByCodeAndChannel(source, realUri);
        if (channelUrl == null) {
            logger.error("安全检查失败,未知source来源");
            responseResult(response, CodeEnum.URL_FAIL);
            return false;
        }*/

        //2.token认证
        /*if (!channelUrl.isNeedTokenAuth()) {
            return true;
        }*/

        // 获取User用户
        /*User user = HttpSessionSuport.getAttribute(SessionConstants.IV_USER);
        if (user == null) {
            logger.warn("安全检查失败,token失效");
            responseResult(response, CodeEnum.SESSION_EXPIRED);
            return false;
        }*/

        return true;
    }

    private void responseResult(HttpServletResponse response, CodeEnum code) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter out = response.getWriter();
        out.append(JSON.toJSONString(APIResponse.createResult(code)));
        out.flush();
        out.close();
    }


}
