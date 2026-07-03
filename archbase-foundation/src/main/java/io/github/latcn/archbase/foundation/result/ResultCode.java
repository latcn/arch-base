package io.github.latcn.archbase.foundation.result;

import io.github.latcn.archbase.core.exception.IErrorCode;

public enum ResultCode implements IErrorCode {

	SUCCESS("00000", "操作成功"), FAIL("B9999", "操作失败");

	private final String code;

	private final String message;

	ResultCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}