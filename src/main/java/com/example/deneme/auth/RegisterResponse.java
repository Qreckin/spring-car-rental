package com.example.deneme.auth;

import java.util.UUID;

public class RegisterResponse {
    private final String message;
    private final UUID customerId;

    public RegisterResponse(String message, UUID customerId) {
        this.message = message;
        this.customerId = customerId;
    }

    public String getMessage() {
        return message;
    }

    public UUID getCustomerId() {
        return customerId;
    }
}