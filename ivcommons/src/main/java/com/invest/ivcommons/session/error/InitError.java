package com.invest.ivcommons.session.error;

/**
 * Created by xugang on 2017/7/29.
 */
public class InitError extends RuntimeException {

    private static final long serialVersionUID = -7440236719115078312L;

    public InitError() {
        super();

    }

    public InitError(String message, Throwable cause,
                     boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

    public InitError(String message, Throwable cause) {
        super(message, cause);

    }

    public InitError(String message) {
        super(message);

    }

    public InitError(Throwable cause) {
        super(cause);

    }


}
