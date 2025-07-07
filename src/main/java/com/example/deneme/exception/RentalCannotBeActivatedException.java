package com.example.deneme.exception;


import java.util.UUID;

public class RentalCannotBeActivatedException extends RuntimeException {

    public RentalCannotBeActivatedException(UUID id) {
        super("Rental with ID " + id + " cannot be activated.");
    }

}