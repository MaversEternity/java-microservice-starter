package com.me.transport.controller.model;

import com.me.exception.ErrorCode;

import lombok.Builder;

@Builder
public record ErrorRs(ErrorCode code, String message) implements Rs {
}
