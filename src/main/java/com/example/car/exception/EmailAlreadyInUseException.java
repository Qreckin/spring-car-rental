package com.example.car.exception;

public class EmailAlreadyInUseException extends RuntimeException{
    public EmailAlreadyInUseException(String email){
        super("Customer with email " + email + " already exists.");
    }
}
