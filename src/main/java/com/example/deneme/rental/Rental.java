package com.example.deneme.rental;

import com.example.deneme.car.Car;
import com.example.deneme.common.BaseEntity;
import com.example.deneme.customer.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

    @Convert(converter = StatusConverter.class)
    private Status status;

    // Be careful! If this order is changed, ORDINAL mapping will be distorted
    public enum Status {
        RESERVED(0),
        ACTIVE(1),
        COMPLETED(2),
        CANCELLED(3);

        private final int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static Status fromCode(int code) {
            for (Status s : values()) {
                if (s.getCode() == code) return s;
            }
            throw new IllegalArgumentException("Invalid Status code: " + code);
        }
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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
}
