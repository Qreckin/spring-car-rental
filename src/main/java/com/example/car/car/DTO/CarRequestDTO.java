package com.example.car.car.DTO;

import com.example.car.enums.Enums;
import com.example.car.validation.NotEmptyIfPresent;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.*;
public class CarRequestDTO {
    @NotEmptyIfPresent
    private String make;

    @NotEmptyIfPresent
    private String model;

    @NotEmptyIfPresent
    private String color;

    @Min(value = 1886, message = "Year must not be earlier than 1886")
    @Max(value = 2025, message = "Year must not be later than 2025")
    private Integer year;

    @Min(value = 0, message = "Required license must be at least 0")
    private Integer requiredLicenseYear;

    @DecimalMin(value = "0.0", inclusive = false, message = "Daily price must be greater than 0")
    private Double dailyPrice;

    @Min(value = 0, message = "Kilometer must be greater than or equal to 0")
    private Integer kilometer;

    @NotEmptyIfPresent
    private String category;

    @Min(value = 0, message = "Gear type must be either 0 or 1")
    @Max(value = 1, message = "Gear type must be either 0 or 1")
    private Integer gearType;

    @NotEmptyIfPresent
    private String licensePlate;

    public CarRequestDTO() {
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
}