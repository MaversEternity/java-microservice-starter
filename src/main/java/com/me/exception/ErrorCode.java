package com.me.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "not found"),
    VALIDATION(HttpStatus.BAD_REQUEST, "validation error"),
    MISSING_HEADER(HttpStatus.BAD_REQUEST, "missing header"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "unauthorized"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "bad request"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error");

    private final HttpStatus status;
    private final String message;
}
