package com.invest.ivcommons.session.http;

import com.invest.ivcommons.session.redis.RedisSession;
import com.invest.ivcommons.session.redis.RedisSessionRepository;
import com.invest.ivcommons.session.utils.HttpConstants;
import com.invest.ivcommons.session.utils.HttpStrategyUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by xugang on 2017/7/29.
 */
public class HttpSessionFilter implements Filter {

    RedisSessionRepository redisSessionRepository;

    public HttpSessionFilter() {

    }

    public HttpSessionFilter(RedisSessionRepository redisSessionRepository) {
        this.redisSessionRepository = redisSessionRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;
        String sessionId = null;
        RedisSession redisSession = null;
        // 判断资源类型
        String filterValue = req.getHeader(HttpConstants.HEADER__FILTER_KEY);
        if (StringUtils.isNotBlank(filterValue)) {
            // filter-cookie
            if (HttpConstants.FILTER_COOKIE.equals(filterValue.trim())) {
                sessionId = HttpStrategyUtil.cookieStrategy.getRequestedSessionId(req);
            }
            // filter-header
            if (HttpConstants.FILTER_HEADER.equals(filterValue.trim())) {
                sessionId = HttpStrategyUtil.headerStrategy.getRequestedSessionId(req);
            }
            if (StringUtils.isNotBlank(sessionId)) {

                redisSession = redisSessionRepository.getSession(sessionId);
            }
            if (StringUtils.isBlank(sessionId) || redisSession == null) {

                redisSession = redisSessionRepository.createSession();
            }
            HttpSessionSuport.set(redisSession);
            HttpSessionSuport.setAttribute(HttpConstants.HEADER__FILTER_KEY, filterValue);
            redisSession.handlClientStore(rep);
            //调用controller
            chain.doFilter(req, rep);
            // 提交session数据
            commitSession(filterValue, HttpSessionSuport.get(), req, rep);

        } else {
            chain.doFilter(request, response);
        }

    }

    private void commitSession(String filterValue, RedisSession session, HttpServletRequest request,
                               HttpServletResponse response) {

        session.cleanClientStore();
        if (null != session && session.getChangeAttrs().size() > 0) {
            if (session.isFirst()) {
                session.addBasicAttrs();
            }
            redisSessionRepository.save(session);
        }
        if (session.isDelete()) {
            redisSessionRepository.delete(session.getId());
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

}
