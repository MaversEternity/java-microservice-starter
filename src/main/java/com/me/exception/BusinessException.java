package com.me.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public BusinessException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
        this.message = message;
    }

    public static BusinessException of(ErrorCode code) {
        return new BusinessException(code);
    }

    public static BusinessException notFound(Class<?> type, Object id) {
        String message = "%s %s by %s".formatted(type.getSimpleName(), ErrorCode.NOT_FOUND.getMessage(), id);
        return new BusinessException(message, ErrorCode.NOT_FOUND);
    }

}
