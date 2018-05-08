package com.invest.ivppad.common.exception;

/**
 * open api 调用异常类
 * Created by xugang on 2017/8/1.
 */
public class PPDOpenApiInvokeException extends RuntimeException {
    public PPDOpenApiInvokeException() {
    }

    public PPDOpenApiInvokeException(String message) {
        super(message);
    }

    public PPDOpenApiInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PPDOpenApiInvokeException(Throwable cause) {
        super(cause);
    }

    public PPDOpenApiInvokeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
