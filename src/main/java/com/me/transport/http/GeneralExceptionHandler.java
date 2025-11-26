package com.me.transport.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.me.exception.BusinessException;
import com.me.exception.ErrorCode;
import com.me.transport.http.model.ErrorRs;
import com.me.transport.http.model.ErrorValidationRs;
import com.me.transport.http.model.Rs;
import com.me.util.JsonUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler implements AuthenticationEntryPoint {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<List<ErrorRs>> handleAppException(BusinessException e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(List.of(new ErrorRs(e.getErrorCode(), e.getMessage())), e.getErrorCode().getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<List<ErrorRs>> handleException(Exception e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(List.of(new ErrorRs(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage())), ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ErrorValidationRs>> handleAppException(ConstraintViolationException e) {
        List<ErrorValidationRs> errors = new ArrayList<>();
        e.getConstraintViolations().forEach(it -> {
            String fieldName = it.getPropertyPath().toString();
            Object rejectedValue = it.getInvalidValue();
            String errorMessage = it.getMessage();

            errors.add(ErrorValidationRs.builder()
                .code(ErrorCode.VALIDATION)
                .field(fieldName)
                .rejectedValue("%s".formatted(rejectedValue))
                .message(errorMessage + ". Rejected value - " + rejectedValue)
                .build());
        });

        return new ResponseEntity<>(errors, ErrorCode.VALIDATION.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {
        List<? extends Rs> errors = ex.getBindingResult().getAllErrors().stream().map(error -> {
                if (error instanceof FieldError e) {
                    return ErrorValidationRs.builder()
                        .code(ErrorCode.VALIDATION)
                        .field(e.getField())
                        .rejectedValue("%s".formatted(e.getRejectedValue()))
                        .message(error.getDefaultMessage() + ". Rejected value - " + e.getRejectedValue())
                        .build();
                } else {
                    return ErrorRs.builder()
                        .code(ErrorCode.VALIDATION)
                        .message(error.getDefaultMessage())
                        .build();
                }
            })
            .toList();

        return new ResponseEntity<>(errors, headers, status);
    }


    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {
        log.error(ex.getMessage(), ex);

        Object[] args;

        if (ex instanceof MethodArgumentTypeMismatchException e) {
            args = new Object[] {e.getName(), ex.getValue()};
        } else {
            args = new Object[] {ex.getPropertyName(), ex.getValue()};
        }

        ErrorValidationRs response = ErrorValidationRs.builder()
            .code(ErrorCode.VALIDATION)
            .field(args[0].toString())
            .rejectedValue("%s".formatted(args[1]))
            .message("Failed to convert '%s' with value: '%s'".formatted(args[0], args[1]))
            .build();

        return new ResponseEntity<>(List.of(response), headers, ErrorCode.VALIDATION.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {
        log.error(ex.getMessage(), ex);

        ErrorRs response = ErrorRs.builder()
            .code(ErrorCode.BAD_REQUEST)
            .message(ex.getMessage())
            .build();
        return new ResponseEntity<>(List.of(response), headers, ErrorCode.BAD_REQUEST.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {
        log.error(ex.getMessage(), ex);

        ErrorValidationRs response = ErrorValidationRs.builder()
            .code(ErrorCode.BAD_REQUEST)
            .field(ex.getParameterName())
            .message("Required request parameter '%s' is missing".formatted(ex.getParameterName()))
            .build();
        return new ResponseEntity<>(List.of(response), headers, ErrorCode.BAD_REQUEST.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
        Object body,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(List.of(ErrorRs.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message(ex.getMessage())), headers, status);
    }

    //Authentication
    @Override
    public void commence(HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException) throws IOException {
        if (response.isCommitted()) {
            logger.trace("Did not write to response since already committed");
            return;
        }

        List<ErrorRs> errorResponse = List.of(ErrorRs.builder().code(ErrorCode.UNAUTHORIZED).build());

        response.setStatus(ErrorCode.UNAUTHORIZED.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.getWriter().write(JsonUtils.convertToString(errorResponse));
        response.getWriter().flush();
    }
}
