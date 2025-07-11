package com.example.car.rental.DTO;


import com.example.car.enums.Enums;
import com.example.car.rental.Rental;

import java.time.LocalDateTime;
import java.util.UUID;

public class RentalDTO {
    private final UUID id;

    private final LocalDateTime rentalStartDate;
    private final LocalDateTime rentalEndDate;

    private final Enums.Status status;

    private final UUID carID;

    private final UUID customerID;

    private final LocalDateTime activatedAt;

    private final LocalDateTime completedAt;

    public RentalDTO(Rental rental){
        this.id = rental.getId();
        this.rentalStartDate = rental.getRentalStartDate();
        this.rentalEndDate = rental.getRentalEndDate();
        this.status = rental.getStatus();
        this.carID = rental.getCar().getId();
        this.customerID = rental.getCustomer().getId();
        this.activatedAt = rental.getActivatedAt();
        this.completedAt = rental.getCompletedAt();
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getRentalStartDate() {
        return rentalStartDate;
    }

    public LocalDateTime getRentalEndDate() {
        return rentalEndDate;
    }

    public Enums.Status getStatus() {
        return status;
    }

    public UUID getCarID() {
        return carID;
    }

    public UUID getCustomerID() {
        return customerID;
    }

    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
}
