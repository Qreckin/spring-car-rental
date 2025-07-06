package com.example.deneme.rental.dto;

import jakarta.validation.constraints.NotNull;

public class RentalActionRequestDTO {

    @NotNull(message = "Action must not be null")
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
