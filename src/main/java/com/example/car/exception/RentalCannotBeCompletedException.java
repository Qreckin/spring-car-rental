package com.example.car.exception;

import java.util.UUID;

public class RentalCannotBeCompletedException extends RuntimeException{

    public RentalCannotBeCompletedException(UUID id){
        super("Rental with ID " + id + " cannot be completed");
    }
}
