package com.example.car.car;

import com.example.car.car.DTO.CarRequestDTO;
import com.example.car.common.BaseEntity;
import com.example.car.rental.Rental;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

// Maps this class to a table in the database
// We can customize table name with @Table(name = "my_table") otherwise its "car"
// Spring maps each field to a column in the table
@Entity
public class Car extends BaseEntity {

    @Id // This declares the primary key of the entity
    @GeneratedValue(strategy = GenerationType.UUID) // Automatically increment id
    private UUID id; // ID of the car in the database
    private String make;
    private String model;
    private String color;
    private Integer year;

    private Integer requiredLicenseYear;

    private Double dailyPrice;

    private Integer kilometer;

    private String category;

    private String gearType;
    private String licensePlate;


    // mappedBy indicates that the "car" field in the Rental entity owns the relationship.
    // cascade = CascadeType.ALL ensures that any operation (persist, merge, remove, etc.) on Car
    // will automatically be applied to its associated Rentals.
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<Rental> rentals;

    // This is needed if we are going to use @Entity

    public Car(){

    }
    public Car(CarRequestDTO carRequestDTO) {
        setMake(carRequestDTO.getMake());
        setModel(carRequestDTO.getModel());
        setColor(carRequestDTO.getColor());
        setYear(carRequestDTO.getYear());
        setRequiredLicenseYear(carRequestDTO.getRequiredLicenseYear());
        setDailyPrice(carRequestDTO.getDailyPrice());
        setKilometer(carRequestDTO.getKilometer());
        setCategory(carRequestDTO.getCategory());
        setLicensePlate(carRequestDTO.getLicensePlate());
        setGearType(carRequestDTO.getGearType());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getRequiredLicenseYear() {
        return requiredLicenseYear;
    }

    public void setRequiredLicenseYear(Integer requiredLicenseYear) {
        this.requiredLicenseYear = requiredLicenseYear;
    }

    public Double getDailyPrice() {
        return dailyPrice;
    }

    public void setDailyPrice(Double dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }

    public Integer getKilometer() {
        return kilometer;
    }

    public void setKilometer(Integer kilometer) {
        this.kilometer = kilometer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGearType() {
        return gearType;
    }

    public void setGearType(String gearType) {
        this.gearType = gearType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
