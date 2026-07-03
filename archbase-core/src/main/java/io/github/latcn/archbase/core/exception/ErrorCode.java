package io.github.latcn.archbase.core.exception;

public enum ErrorCode implements IErrorCode {

	HANDLER_NOT_FOUND("ARCH-001", "未找到对应的处理器"), ENTITY_NOT_FOUND("ARCH-002", "实体不存在"),
	INVALID_PARAMETER("ARCH-003", "参数校验失败"), SYSTEM_ERROR("ARCH-999", "系统内部错误");

	private final String code;

	private final String message;

	ErrorCode(String code, String message) {
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