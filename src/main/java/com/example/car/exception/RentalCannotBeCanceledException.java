package com.example.car.exception;

import java.util.UUID;

public class RentalCannotBeCanceledException extends RuntimeException{

    public RentalCannotBeCanceledException(UUID id){
        super("Rental with ID " + id + " cannot be cancelled");
    }
}
