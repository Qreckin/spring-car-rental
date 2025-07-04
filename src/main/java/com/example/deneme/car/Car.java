package com.example.deneme.car;

import com.example.deneme.rental.Rental;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

// Maps this class to a table in the database
// We can customize table name with @Table(name = "my_table") otherwise its "car"
// Spring maps each field to a column in the table
@Entity
public class Car {

    // Data fields are private for encapsulation
    @Id // This declares the primary key of the entity
    @GeneratedValue(strategy = GenerationType.UUID) // Automatically increment id
    private UUID id; // ID of the car in the database
    private String make; // Make of the car
    private String model; // Model of the car
    private Integer year; // Year of the car

    private List<UUID> rentalIds;

    // This is needed if we are going to use @Entity
    public Car() {
    }

    public UUID getId() {
        return id;
    }


    public void setRentalIds(List<UUID> rentalIds) {
        this.rentalIds = rentalIds;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<UUID> getRentalIds() {
        return rentalIds;
    }

}
