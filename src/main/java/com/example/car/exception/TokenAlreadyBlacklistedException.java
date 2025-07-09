package com.example.car.exception;

public class TokenAlreadyBlacklistedException extends RuntimeException {
    public TokenAlreadyBlacklistedException(String message) {
        super(message);
    }
}