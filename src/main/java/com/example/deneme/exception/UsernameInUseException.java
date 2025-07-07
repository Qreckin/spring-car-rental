package com.example.deneme.exception;

public class UsernameInUseException extends RuntimeException{
    public UsernameInUseException(String username){
        super("Customer with username: " + username + " already exists");
    }
}
