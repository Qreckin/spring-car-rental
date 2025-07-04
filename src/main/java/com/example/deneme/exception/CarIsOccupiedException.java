package com.example.deneme.exception;

import java.util.UUID;

public class CarIsOccupiedException extends RuntimeException{

    public CarIsOccupiedException(UUID id){
        super("Car with ID " + id + " is occupied during this time interval.");
    }
}
