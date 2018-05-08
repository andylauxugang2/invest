package com.zhuobao.ivsyncjob.common.error;

/**
 * Created by xugang on 2017/8/6.
 */
public class ElasticExecuteError extends Exception {
    private static final long serialVersionUID = 4682140151788042720L;

    public ElasticExecuteError() {
        super();
    }

    public ElasticExecuteError(String message) {
        super(message);
    }

    public ElasticExecuteError(String message, Throwable cause) {
        super(message, cause);
    }

    public ElasticExecuteError(Throwable cause) {
        super(cause);
    }

    protected ElasticExecuteError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
