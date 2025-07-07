package com.example.deneme.rental;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class RentalRequestDTO {

    @NotNull(message = "Car ID must not be null")
    private UUID carId;

    @NotNull(message = "Customer ID must not be null")
    private UUID customerId;

    @NotNull(message = "Date must not be null")
    private LocalDateTime rentalStartDate;
    @NotNull(message = "Date must not be null")
    private LocalDateTime rentalEndDate;


    public RentalRequestDTO(){

    }

    public RentalRequestDTO(UUID carId, UUID customerId, LocalDateTime rentalStartDate, LocalDateTime rentalEndDate) {
        this.carId = carId;
        this.customerId = customerId;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
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

    public LocalDateTime getRentalStartDate() {
        return rentalStartDate;
    }

    public void setRentalStartDate(LocalDateTime rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    public LocalDateTime getRentalEndDate() {
        return rentalEndDate;
    }

    public void setRentalEndDate(LocalDateTime rentalEndDate) {
        this.rentalEndDate = rentalEndDate;
    }

}
