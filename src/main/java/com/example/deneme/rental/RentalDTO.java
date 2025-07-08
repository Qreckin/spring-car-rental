package com.example.deneme.rental;


import java.time.LocalDateTime;
import java.util.UUID;

public class RentalDTO {
    private UUID id;

    private LocalDateTime rentalStartDate;
    private LocalDateTime rentalEndDate;

    private Rental.Status status;

    private UUID carID;

    private UUID customerID;

    public RentalDTO(Rental rental){
        this.id = rental.getId();
        this.rentalStartDate = rental.getRentalStartDate();
        this.rentalEndDate = rental.getRentalEndDate();
        this.status = rental.getStatus();
        this.carID = rental.getCar().getId();
        this.customerID = rental.getCustomer().getId();
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

    public Rental.Status getStatus() {
        return status;
    }

    public UUID getCarID() {
        return carID;
    }

    public UUID getCustomerID() {
        return customerID;
    }
}
