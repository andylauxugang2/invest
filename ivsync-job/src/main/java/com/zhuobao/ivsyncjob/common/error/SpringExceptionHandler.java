package com.zhuobao.ivsyncjob.common.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * Created by xugang on 2017/8/4.
 */
public class SpringExceptionHandler implements AsyncUncaughtExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(SpringExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method,
                                        Object... params) {
        logger.error(method.getName() + "is fail", ex);
        ex.printStackTrace();
    }

}
