package com.invest.ivcommons.session.error;

/**
 * Created by xugang on 2017/7/29.
 */
public class ParamError extends RuntimeException{

	private static final long serialVersionUID = -7440236719115078312L;

	public ParamError() {
		super();

	}

	public ParamError(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	public ParamError(String message, Throwable cause) {
		super(message, cause);

	}

	public ParamError(String message) {
		super(message);

	}

	public ParamError(Throwable cause) {
		super(cause);

	}



}
