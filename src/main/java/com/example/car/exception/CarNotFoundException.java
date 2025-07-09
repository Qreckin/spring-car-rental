package com.example.car.exception;

import java.util.UUID;

public class CarNotFoundException extends RuntimeException{
    public CarNotFoundException(UUID id){
        super("Car with ID " + id + " not found");
    }
}
