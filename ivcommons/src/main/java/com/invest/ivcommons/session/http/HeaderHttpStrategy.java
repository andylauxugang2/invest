package com.invest.ivcommons.session.http;

import com.invest.ivcommons.session.Session;
import com.invest.ivcommons.session.utils.HttpConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xugang on 2017/7/29.
 */
public class HeaderHttpStrategy implements HttpStrategy {

    public static String headerName = HttpConstants.HEADER_SESSION_NAME; //sessionId eg:6F44C365A02D897F06FA310CDE2033D7

    @Override
    public String getRequestedSessionId(HttpServletRequest request) {

        return request.getHeader(headerName);
    }

    @Override
    public void createSession(Session session, HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(headerName, session.getId());
    }

    @Override
    public void invalidateSession(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(headerName, "");
    }

}
