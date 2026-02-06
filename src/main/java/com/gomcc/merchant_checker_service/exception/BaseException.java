package com.gomcc.merchant_checker_service.exception;

public class BaseException extends RuntimeException{
    /**
     * Implement Unchecked exception by extending RuntimeException in order to prevent adding "throws exception" statement in front of every method/class
     */

    public BaseException(String message){
        super(message);
    }
}
