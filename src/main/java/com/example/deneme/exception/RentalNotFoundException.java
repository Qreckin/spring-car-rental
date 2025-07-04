package com.example.deneme.exception;

import java.util.UUID;

public class RentalNotFoundException extends RuntimeException{
    public RentalNotFoundException(UUID id){
        super("Rental with ID " + id + " not found.");
    }
}
