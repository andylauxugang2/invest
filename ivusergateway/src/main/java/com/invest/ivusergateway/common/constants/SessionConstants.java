package com.invest.ivusergateway.common.constants;

import javax.servlet.http.HttpSession;

/**
 * Created by xugang on 2017/7/28.
 */
public class SessionConstants {

    public static final String NICK = "nick";

    public static final String USER_ID = "user_id";

    public static final String CORPORATION_ID = "corporation_id";

    public static final String USER_NAME = "user_name";

    public static void setAttributeNick(HttpSession session, String nick) {
        if (session == null) {
            return;
        }
        session.setAttribute(NICK, nick);
    }

    public static void setAttributeUserId(HttpSession session, Long userId) {
        if (session == null) {
            return;
        }
        session.setAttribute(USER_ID, userId);
    }

    public static void setAttributeUserName(HttpSession session, String UserName) {
        if (session == null) {
            return;
        }
        session.setAttribute(USER_NAME, UserName);
    }

    public static String getAttributeUserName(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute(USER_NAME);
    }

    public static String getAttributeNick(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute(NICK);
    }

    public static Long getAttributeUserId(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (Long) session.getAttribute(USER_ID);
    }

    public static Long getCorporationId(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (Long) session.getAttribute(CORPORATION_ID);
    }


}
