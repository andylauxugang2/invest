package com.invest.ivcommons.web.model;

import org.springframework.core.NamedThreadLocal;

/**
 * 请求上下文 使用ThreadLocal实现 比较简单
 * Created by xugang on 2016/9/6.
 */
public class RequestContext {
    public static final String REQUEST_ID = "IVCOMMONS-WEB-REQUEST-ID";
    public static final String LOGIN_ID = "IVCOMMONS-WEB-LOGIN-ID";
    public static final String API = "IVCOMMONS-WEB-API";
    private String requestId;
    private String loginId;
    private String api;

    private static final String name = "ivcommons-web Request Context Holder";
    private static final ThreadLocal<RequestContext> holder = new NamedThreadLocal<>(name);

    public static void reset() {
        holder.remove();
    }

    public static void set(RequestContext requestContext) {
        if (requestContext == null) {
            reset();
        } else {
            holder.set(requestContext);
        }
    }

    public static RequestContext get() {
        RequestContext requestContext = holder.get();
        return requestContext;
    }

    public static String getCurrentRequestId() {
        RequestContext requestContext = get();
        if (requestContext != null) {
            return requestContext.getRequestId();
        }
        return "unknowned request id";
    }

    public static String getCurrentloginId() {
        RequestContext requestContext = get();
        if (requestContext != null) {
            return requestContext.getLoginId();
        }
        return null;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }
}
