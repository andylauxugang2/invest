package com.invest.ivgateway.interceptor;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivuser.biz.service.UserService;
import com.invest.ivuser.model.result.UserResult;
import com.invest.ivusergateway.common.ViewConstants;
import com.invest.ivusergateway.common.constants.HttpConstant;
import com.invest.ivusergateway.common.constants.SessionConstants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class PassportIntercepter extends BaseInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        logger.info("request with uri '" + uri + "' has been intercepted");

        //验证是否登陆
        Result result = validateToken(request);
        if (result.isSuccess()) {
            //已经登陆
            return super.preHandle(request, response, handler);
        } else {
            //未登陆
            if (isAjaxRequest(request)) {
                //AJAX请求返回错误码
                response.sendError(HttpServletResponse.SC_REQUEST_TIMEOUT, "当前会话未登陆");
                return true;
            } else {
                //非AJAX请求重新登陆
                // redirect
                response.sendRedirect(ViewConstants.VIEW_LOGIN);
                // forward
                // request.getRequestDispatcher(ViewConstants.VIEW_LOGIN).forward(request, response);
            }

            return false;
        }
    }

    /**
     * 使用统一权限系统登陆接口
     * 每次请求地址都要先判断是否登陆到统一权限
     * 如果未登陆调用backurl接口返回登陆页面
     */
    private Result validateToken(HttpServletRequest request) {
        Result result = new Result();
        //验证是否登陆
        String token = getCookie(request, HttpConstant.COOKIE_TOKEN);
        Map<String, String> params = new HashMap<>();

        UserResult userResult = userService.verifyUniWasLoggedin(token, params);
        result.setSuccess(userResult.isSuccess());
        result.setErrorMsg(userResult.getErrorMsg());

        if (userResult.isFailed()) {
            logger.error("Interceptor：verifyUniWasLoggedin failed, errorMsg:" + userResult.getErrorMsg());
        } else {
            SessionConstants.setAttributeUserId(request.getSession(), userResult.getUserId());
        }

        return result;
    }
}
