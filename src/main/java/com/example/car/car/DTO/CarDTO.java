package com.example.car.car.DTO;

import com.example.car.car.Car;
import com.example.car.enums.Enums;
import com.example.car.rental.DTO.RentalDTO;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public class CarDTO {
    private final UUID id;
    private final String make;
    private final String model;
    private final String color;
    private final Integer year;
    private final Integer requiredLicenseYear;
    private final Double dailyPrice;

    private final Integer kilometer;

    private String category;

    private Integer gearType;

    private String licensePlate;
    private Double totalPrice;

    private final List<RentalDTO> rentals;

    // Constructors
    public CarDTO(Car car) {
        this.id = car.getId();
        this.make = car.getMake();
        this.model = car.getModel();
        this.color = car.getColor();
        this.year = car.getYear();
        this.requiredLicenseYear = car.getRequiredLicenseYear();
        this.dailyPrice = car.getDailyPrice();
        this.kilometer = car.getKilometer();
        this.totalPrice = null;
        this.rentals = car.getRentals()
                .stream()
                .map(RentalDTO::new)
                .toList();
        this.category = car.getCategory();
        this.gearType = car.getGearType().getValue();
        this.licensePlate = car.getLicensePlate();
    }

    public UUID getId() {
        return id;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getRequiredLicenseYear() {
        return requiredLicenseYear;
    }

    public Double getDailyPrice() {
        return dailyPrice;
    }

    public Integer getKilometer() {
        return kilometer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getGearType() {
        return gearType;
    }

    public void setGearType(Integer gearType) {
        this.gearType = gearType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<RentalDTO> getRentals() {
        return rentals;
    }
}