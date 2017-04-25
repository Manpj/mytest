package com.tairan.cloud.common;

public enum ErrorCode {

	// -----------------------------------------------------------------------------------

	ILLEGAL_ARGUMENT_ERROR("U_0001", "参数校验未通过"), //
	SYSTEM_ERROR("U_0002", "系统内部异常"), //
	UNKNOWN_ERROR("U_0003", "未知异常"), //
	SESSION_TIMEOUT("U_0004", "当前会话已过期,请重新执行流程"), //
	CONNECTION_TIMEOUT("U_0005", "网络连接超时"), //
	PERMISSION_DENIED("U_0006", "权限拒绝"), //
	ILLEGAL_IDENTITY("U_0007", "身份非法"), //
	GOAL_CONNECTION_ERROR("U_0008", "目标网站连接异常"), //
	GOAL_RESPONSE_ERROR("U_0009", "目标网站响应异常"), //
	AUTH_CODE_DISCERN_ERROR("U_0010", "验证码识别服务异常"), //
	SIGN_IN_FAILED("U_0011", "登录失败,请检查登录参数后重试"), //
	PARSE_EXCEPTION("U_0012", "页面解析异常"), //
	ASPRISE_OCR_ERROR("U_0013", "验证码识别错误，请重试"), //
	ASPRISE_OCR_DISABLE("U_0014", "该类型验证码无法识别"),
	SIGN_IN_FAILED_("U_0015", "登录失败,请检查登录参数后重试"); //没有验证码
	
	public String code;
	public String message;

	private ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
