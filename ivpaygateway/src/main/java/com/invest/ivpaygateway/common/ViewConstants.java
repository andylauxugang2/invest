package com.invest.ivpaygateway.common;

/**
 * Created by xugang on 16/9/9.
 * 用户页面跳转 服务器端 回调 不放在这里 放在动态开关中
 */
public final class ViewConstants {

	public static final String VIEW_BASE = "";

	public static final String VIEW_WEB_LOGIN_PAGE = "/login/login1.cc?gotoUrl=%s&callback=%s&extParam=%s";
	public static final String VIEW_WEB_ERROR_PAGE = "/errors/error";
	public static final String VIEW_WEB_404_PAGE = "404";
	public static final String VIEW_WEB_ONEKEY_LOGIN_PAGE = "/autouser/autologin.cc?username=%s&timestamp=%s&gotoUrl=%s";


}
