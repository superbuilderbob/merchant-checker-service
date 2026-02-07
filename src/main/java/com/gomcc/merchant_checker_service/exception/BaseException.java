package com.gomcc.merchant_checker_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class BaseException extends RuntimeException{
    /**
     * Implement Unchecked exception by extending RuntimeException in order to prevent adding "throws exception" statement in front of every method/class
     */
    private String errorCode;
    private HttpStatus status;
    private String message;
}
