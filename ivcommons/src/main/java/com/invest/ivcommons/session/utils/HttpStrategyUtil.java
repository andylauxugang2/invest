package com.invest.ivcommons.session.utils;

import com.invest.ivcommons.session.http.CookieHttpStrategy;
import com.invest.ivcommons.session.http.HeaderHttpStrategy;
import com.invest.ivcommons.session.http.HttpStrategy;

/**
 * Created by xugang on 2017/7/29.
 */
public class HttpStrategyUtil {
	
	public static HttpStrategy headerStrategy = new HeaderHttpStrategy();
	public static HttpStrategy cookieStrategy = new CookieHttpStrategy();

}
