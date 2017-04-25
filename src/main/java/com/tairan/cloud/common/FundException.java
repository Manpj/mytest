package com.tairan.cloud.common;

public class FundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String code; // 异常对应的返回码
	private String msg; // 异常对应的描述信息

	public FundException() {
		super();
	}

	public FundException(String message) {
		super(message);
		msg = message;
	}

	public FundException(String code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
