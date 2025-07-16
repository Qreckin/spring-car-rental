package com.example.car.rental.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class AddRentalResponse {

    private final UUID ID;

    private final String PNR;

    public AddRentalResponse(UUID ID, String PNR) {
        this.ID = ID;
        this.PNR = PNR;
    }

    public UUID getID() {
        return ID;
    }

    public String getPNR() {
        return PNR;
    }
}
