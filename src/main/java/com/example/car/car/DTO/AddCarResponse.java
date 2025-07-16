package com.example.car.car.DTO;

import com.example.car.CustomResponseEntity;
import com.example.car.enums.Enums;

import java.util.UUID;

public class AddCarResponse {

    private final UUID id;

    private final String plateNo;

    private final CustomResponseEntity code;


    public AddCarResponse(UUID id, String plateNo, CustomResponseEntity code) {
        this.id = id;
        this.plateNo = plateNo;
        this.code = code;
    }

    public UUID getId() {
        return id;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public CustomResponseEntity getCode() {
        return code;
    }
}
