package com.example.car.exception;

import java.time.LocalDateTime;

public class IllegalDateTimeException extends RuntimeException{

    public IllegalDateTimeException(LocalDateTime start, LocalDateTime end){
        super("LocalDateTime pairs START: " + start.toString() + " END: " + end.toString() + " are INVALID");
    }
}
