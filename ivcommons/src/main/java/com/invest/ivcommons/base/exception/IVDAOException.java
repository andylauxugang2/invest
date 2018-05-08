package com.invest.ivcommons.base.exception;

/**
 * Created by xugang on 2017/7/29.
 */
public class IVDAOException extends RuntimeException {
    public IVDAOException() {
    }

    public IVDAOException(String message) {
        super(message);
    }

    public IVDAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public IVDAOException(Throwable cause) {
        super(cause);
    }

    public IVDAOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
