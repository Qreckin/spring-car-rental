package com.example.deneme.exception;

import java.util.UUID;

public class CarAlreadyDeletedException extends RuntimeException{

    public CarAlreadyDeletedException(UUID id){
        super("Car with ID " + id + " has already been deleted.");
    }
}
