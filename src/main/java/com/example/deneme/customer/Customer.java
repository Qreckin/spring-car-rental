package com.example.deneme.customer;

import com.example.deneme.common.BaseEntity;
import com.example.deneme.rental.Rental;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class Customer extends BaseEntity {
    // Data fields are private for encapsulation
    @Id // This declares the primary key of the entity
    @GeneratedValue(strategy = GenerationType.UUID) // Automatically increment id
    private UUID id; // ID of the customer in the database
    private String name; // Name of the customer

    private String email; // Email of the customer

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rental> rentals; // Rents that this user made


    public Customer(){

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }
}
