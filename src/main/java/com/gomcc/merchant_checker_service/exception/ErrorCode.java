package com.gomcc.merchant_checker_service.exception;

import lombok.Getter;

public enum CustomErrorCode {
    TYPE_MISMATCH("TYPE_MISMATCH");

    @Getter
    private final String errorCode;

    CustomErrorCode(String errorCode){
        this.errorCode = errorCode;
    }
}
