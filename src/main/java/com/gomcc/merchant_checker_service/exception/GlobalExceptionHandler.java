package com.gomcc.merchant_checker_service.exception;


import com.gomcc.merchant_checker_service.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            HttpServletRequest request,
            ConstraintViolationException ex
    ){

        Optional<ConstraintViolation<?>> violation = ex.getConstraintViolations()
                .stream()
                .findFirst();

        ErrorResponse error;

        if (violation.isPresent()){
            error = ErrorResponse.builder()
                    .errorCode(ErrorCode.VALIDATION_ERROR.getErrorCode())
                    .message("You have provided: (" + violation.get().getInvalidValue().toString() + "). " +
                            "Please retry with the acceptable regex /[0-9a-zA-Z ]+/")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .timestamp(Instant.now())
                    .path(request.getRequestURI())
                    .build();
        } else{
            error = ErrorResponse.builder()
                    .errorCode(ErrorCode.VALIDATION_ERROR.getErrorCode())
                    .message(ex.getMessage())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .timestamp(Instant.now())
                    .path(request.getRequestURI())
                    .build();
        }

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            HttpServletRequest request,
            ResourceNotFoundException ex
    ){
        ErrorResponse error = ErrorResponse.builder()
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .status(ex.getStatus().value())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            HttpServletRequest request,
            MethodArgumentTypeMismatchException ex
    ){
        ErrorResponse error = ErrorResponse.builder()
                .errorCode(ErrorCode.TYPE_MISMATCH.getErrorCode())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}

