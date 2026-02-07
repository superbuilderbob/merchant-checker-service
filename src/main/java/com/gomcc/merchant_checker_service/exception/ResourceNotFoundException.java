package com.gomcc.merchant_checker_service.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String errorCode, HttpStatus status, String message) {
        super(errorCode, status, message);
    }
}
