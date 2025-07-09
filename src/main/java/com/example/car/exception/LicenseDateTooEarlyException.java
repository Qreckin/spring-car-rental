package com.example.car.exception;

import java.util.UUID;

public class LicenseDateTooEarlyException extends RuntimeException{
    public LicenseDateTooEarlyException(UUID id){
        super("Customer with ID: " + id + " cannot legally drive this car");
    }
}
