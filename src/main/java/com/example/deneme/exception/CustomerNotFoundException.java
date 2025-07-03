package com.example.deneme.exception;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(UUID id){
        super("Customer with ID " + id + " not found");
    }
}
