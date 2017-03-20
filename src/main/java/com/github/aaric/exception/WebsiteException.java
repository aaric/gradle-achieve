package com.github.aaric.exception;

/**
 * 异常类
 * 
 * @author Aaric
 * @since 2017-03-20
 */
@SuppressWarnings("serial")
public class WebsiteException extends Exception {

	private String errorCode = "0000";

	private Object data = null;

	public WebsiteException(String errorCode) {
		this.errorCode = errorCode;
	}

	public WebsiteException(String errorCode, Object data) {
		super();
		this.errorCode = errorCode;
		this.data = data;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public Object getData() {
		return data;
	}

}
