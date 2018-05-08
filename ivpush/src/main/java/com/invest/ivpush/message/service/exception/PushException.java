package com.invest.ivpush.message.service.exception;

/**
 * Created by xugang on 2017/7/27.
 */
public class PushException extends RuntimeException {
    public PushException() {
    }

    public PushException(String message) {
        super(message);
    }

    public PushException(String message, Throwable cause) {
        super(message, cause);
    }

    public PushException(Throwable cause) {
        super(cause);
    }

    public PushException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
