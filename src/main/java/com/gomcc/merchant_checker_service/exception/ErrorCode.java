package com.gomcc.merchant_checker_service.exception;

import lombok.Getter;

public enum ErrorCode {
    TYPE_MISMATCH("TYPE_MISMATCH"),
    NOT_FOUND("NOT_FOUND"),
    VALIDATION_ERROR("VALIDATION_ERROR");

    @Getter
    private final String errorCode;

    ErrorCode(String errorCode){
        this.errorCode = errorCode;
    }
}
