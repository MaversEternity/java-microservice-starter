package com.me.transport.http.model;

import com.me.exception.ErrorCode;

import lombok.Builder;

@Builder
public record ErrorValidationRs(ErrorCode code,
                                String message,
                                String field,
                                String rejectedValue) implements Rs {
}
