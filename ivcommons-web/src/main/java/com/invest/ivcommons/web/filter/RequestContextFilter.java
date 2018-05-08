package com.invest.ivcommons.web.filter;

import com.invest.ivcommons.web.model.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by xugang on 2016/9/6.
 */
public class RequestContextFilter extends OncePerRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(RequestContextFilter.class);

    @Override
    protected void initFilterBean() throws ServletException {

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        initContextHolders(request);

        try {
            filterChain.doFilter(request, response);
        } finally {
            resetContextHolders();
            if (logger.isDebugEnabled()) {
                logger.debug("Cleared thread-bound request context: " + request);
            }
        }
    }

    private void initContextHolders(HttpServletRequest request) {
        RequestContext context = new RequestContext();

        String requestId = request.getHeader(RequestContext.REQUEST_ID);
        if (requestId == null) {
            requestId = UUID.randomUUID().toString();
        }

        context.setRequestId(requestId);

        String loginId = request.getHeader(RequestContext.LOGIN_ID);
        context.setLoginId(loginId);

        String api = request.getHeader(RequestContext.API);
        context.setApi(api);

        if (logger.isDebugEnabled()) {
            logger.debug("init request context: " + context);
        }

        RequestContext.set(context);
    }

    private void resetContextHolders() {
        RequestContext.reset();
    }
}
