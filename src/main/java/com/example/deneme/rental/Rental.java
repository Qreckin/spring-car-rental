package com.example.deneme.rental;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Rental {
    // Data fields are private for encapsulation
    @Id // This declares the primary key of the entity
    @GeneratedValue(strategy = GenerationType.UUID) // Automatically increment id
    private UUID id;

    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;

    private String status;


    private UUID carId;


    private UUID customerId;

    public Rental(){}


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}
