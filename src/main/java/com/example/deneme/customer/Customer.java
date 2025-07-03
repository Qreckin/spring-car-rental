package com.example.deneme.customer;

import com.example.deneme.rental.Rental;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

@Entity
public class Customer {
    // Data fields are private for encapsulation
    @Id // This declares the primary key of the entity
    @GeneratedValue(strategy = GenerationType.UUID) // Automatically increment id
    private UUID id; // ID of the customer in the database
    private String name; // Name of the customer

    private String email; // Email of the customer
    private List<UUID> rentalIds; // Rents that this user made

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

    public List<UUID> getRentalIds() {
        return rentalIds;
    }

    public void setRentalIds(List<UUID> rentalIds) {
        this.rentalIds = rentalIds;
    }
}
