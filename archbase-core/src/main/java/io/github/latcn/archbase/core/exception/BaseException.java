package io.github.latcn.archbase.core.exception;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaseException extends RuntimeException {
    private final String code;
    private final Map<String, Object> context = new LinkedHashMap<>();

    private BaseException(IErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.code = errorCode.getCode();
    }

    private BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public static BaseException of(IErrorCode errorCode) {
        return new BaseException(errorCode, null);
    }

    public static BaseException wrap(Throwable cause, IErrorCode errorCode) {
        return new BaseException(errorCode, cause);
    }

    public static BaseException of(IErrorCode errorCode, String customMessage) {
        BaseException ex = new BaseException(errorCode, null);
        return ex.set("detail", customMessage);
    }

    public static BaseException of(String code, String message) {
        return new BaseException(code, message, null);
    }

    public BaseException set(String key, Object value) {
        this.context.put(key, value);
        return this;
    }

    public BaseException set(Map<String, Object> context) {
        this.context.putAll(context);
        return this;
    }

    public String getCode() {
        return code;
    }

    public Map<String, Object> getContext() {
        return Collections.unmodifiableMap(context);
    }
}