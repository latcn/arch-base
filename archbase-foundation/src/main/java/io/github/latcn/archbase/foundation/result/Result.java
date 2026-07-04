package io.github.latcn.archbase.foundation.result;

import io.github.latcn.archbase.core.exception.IErrorCode;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Result<T> {

	private String code;

	private String msg;

	private T data;

	private LocalDateTime timestamp;

	private Result() {
		this.timestamp = LocalDateTime.now();
	}

	public static <T> Result<T> success(T data) {
		Result<T> result = new Result<>();
		result.code = ResultCode.SUCCESS.getCode();
		result.msg = ResultCode.SUCCESS.getMessage();
		result.data = data;
		return result;
	}

	public static <T> Result<T> success() {
		return success(null);
	}

	public static <T> Result<T> fail(String code, String msg) {
		Result<T> result = new Result<>();
		result.code = code;
		result.msg = msg;
		return result;
	}

	public static <T> Result<T> fail(IErrorCode errorCode) {
		return fail(errorCode.getCode(), errorCode.getMessage());
	}

	public static <T> Result<T> fail(IErrorCode errorCode, T data) {
		Result<T> result = fail(errorCode);
		result.data = data;
		return result;
	}

	public boolean isSuccess() {
		return ResultCode.SUCCESS.getCode().equals(code);
	}

	@Override
	public String toString() {
		return "Result{" + "code='" + code + '\'' + ", msg='" + msg + '\'' + ", data=" + data + ", timestamp="
				+ timestamp + '}';
	}

}