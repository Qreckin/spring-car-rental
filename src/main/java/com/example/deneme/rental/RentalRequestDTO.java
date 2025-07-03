package com.example.deneme.rental;

import java.time.LocalDate;
import java.util.UUID;

public class RentalRequestDTO {
    private UUID carId;
    private UUID customerId;
    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;

    private String status;

    public RentalRequestDTO(){

    }

    public RentalRequestDTO(UUID carId, UUID customerId, LocalDate rentalStartDate, LocalDate rentalEndDate, String status) {
        this.carId = carId;
        this.customerId = customerId;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
        this.status = status;
    }


    public UUID getCarId() {
        return carId;
    }

    public void setCarId(UUID carId) {
        this.carId = carId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public LocalDate getRentalStartDate() {
        return rentalStartDate;
    }

    public void setRentalStartDate(LocalDate rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    public LocalDate getRentalEndDate() {
        return rentalEndDate;
    }

    public void setRentalEndDate(LocalDate rentalEndDate) {
        this.rentalEndDate = rentalEndDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
