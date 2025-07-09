package com.example.car.auth;

import java.util.UUID;

public class RegisterResponse {
    private final String message;
    private final UUID customerId;
    private final String token; // New field

    public RegisterResponse(String message, UUID customerId, String token) {
        this.message = message;
        this.customerId = customerId;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getToken() {
        return token;
    }
}