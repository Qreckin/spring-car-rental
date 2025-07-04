package com.example.deneme.exception;

import java.util.UUID;

public class RentalAlreadyCompletedException extends RuntimeException{
    public RentalAlreadyCompletedException(UUID id){
        super("Rental with ID " + id + "has already been completed.");
    }
}
