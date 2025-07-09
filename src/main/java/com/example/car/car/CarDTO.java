package com.example.car.car;

import com.example.car.rental.RentalDTO;
import java.util.List;
import java.util.UUID;

public class CarDTO {
    private UUID id;
    private String make;
    private String model;
    private String color;
    private Integer year;
    private Integer requiredLicenseYear;
    private Double dailyPrice;

    private Integer kilometer;
    private Double totalPrice;

    private List<RentalDTO> rentalsDTOs;

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
        this.rentalsDTOs = car.getRentals()
                .stream()
                .map(RentalDTO::new)
                .toList();
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

    public Double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(Double totalPrice){
        this.totalPrice = totalPrice;
    }

    public List<RentalDTO> getRentalsDTOs() {
        return rentalsDTOs;
    }
}