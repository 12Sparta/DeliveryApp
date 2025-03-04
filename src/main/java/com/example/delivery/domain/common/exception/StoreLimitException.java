package com.example.delivery.domain.common.exception;

public class StoreLimitException extends RuntimeException{
    public StoreLimitException(String message) {
        super(message);
    }

}
