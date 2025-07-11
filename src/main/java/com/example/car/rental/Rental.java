package com.example.car.rental;

import com.example.car.car.Car;
import com.example.car.common.BaseEntity;
import com.example.car.customer.Customer;
import com.example.car.enums.Enums;
import com.example.car.enums.StatusConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
public class Rental extends BaseEntity {
    // Data fields are private for encapsulation
    @Id // This declares the primary key of the entity
    @GeneratedValue(strategy = GenerationType.UUID) // Automatically increment id
    private UUID id;

    private LocalDateTime rentalStartDate;
    private LocalDateTime rentalEndDate;

    private Double totalPricePaidByCustomer;

    @Convert(converter = StatusConverter.class)
    private Enums.Status status;

    private LocalDateTime activatedAt;

    private LocalDateTime completedAt;

    // Be careful! If this order is changed, ORDINAL mapping will be distorted


    // With:
    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    @JsonIgnore
    private Car car;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonIgnore
    private Customer customer;

    public Rental(){}


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Enums.Status getStatus() {
        return status;
    }

    public void setStatus(Enums.Status status) {
        this.status = status;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Double getTotalPricePaidByCustomer() {
        return totalPricePaidByCustomer;
    }

    public void setTotalPricePaidByCustomer(Double totalPricePaidByCustomer) {
        this.totalPricePaidByCustomer = totalPricePaidByCustomer;
    }

    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(LocalDateTime activatedAt) {
        this.activatedAt = activatedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
