package io.github.latcn.archbase.web.spring.exception;

import io.github.latcn.archbase.core.exception.BaseException;
import io.github.latcn.archbase.core.spi.CustomExceptionHandler;
import io.github.latcn.archbase.foundation.result.Result;
import io.github.latcn.archbase.foundation.result.ResultCode;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public abstract class GlobalExceptionHandler {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired(required = false)
	private CustomExceptionHandler customExceptionHandler;

	@ExceptionHandler(BaseException.class)
	@ResponseBody
	public Result<?> handleBaseException(BaseException e) {
		log.warn("BaseException: code={}, msg={}, context={}", e.getCode(), e.getMessage(), e.getContext());

		if (customExceptionHandler != null) {
			Object result = customExceptionHandler.handle(e);
			if (result instanceof Result) {
				return (Result<?>) result;
			}
		}
		return Result.fail(e.getCode(), e.getMessage());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	public Result<?> handleConstraintViolation(ConstraintViolationException e) {
		log.warn("Validation failed: {}", e.getMessage());
		String message = e.getConstraintViolations()
			.stream()
			.map(v -> v.getPropertyPath() + ": " + v.getMessage())
			.collect(Collectors.joining("; "));
		return Result.fail(ResultCode.FAIL.getCode(), message);
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Result<?> handleGenericException(Exception e) {
		log.error("Unexpected error", e);
		return Result.fail("500", "系统内部错误");
	}

	protected void logError(Exception e) {
		log.error("Exception occurred", e);
	}

	protected String mapToErrorCode(Exception e) {
		return "500";
	}

}