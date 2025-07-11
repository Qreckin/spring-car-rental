package com.example.car.car.DTO;

import com.example.car.enums.Enums;
import jakarta.validation.constraints.*;
public class CarRequestDTO {

    @NotBlank(message = "Make must not be blank")
    private String make;

    @NotBlank(message = "Model must not be blank")
    private String model;

    @NotBlank(message = "Color must not be blank")
    private String color; // Optional: no validation added

    @NotNull(message = "Year must not be null")
    @Min(value = 1886, message = "Year must not be earlier than 1886")
    @Max(value = 2025, message = "Year must not be later than 2025")
    private Integer year;

    @NotNull(message = "Required license must not be null")
    @Min(value = 0, message = "Required license must be at least 0")
    private Integer requiredLicenseYear;

    @NotNull(message = "Daily price must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Daily price must be greater than 0")
    private Double dailyPrice;

    @Min(value = 0, message = "Kilometer must be greater than or equal to 0")
    @NotNull(message = "Kilometer must not be null")
    private Integer kilometer;

    @NotBlank(message = "Category must not be blank")
    private String category;

    private Enums.GearType gearType;

    @NotBlank(message = "License plate must not be blank")
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

    public Enums.GearType getGearType() {
        return gearType;
    }

    public void setGearType(Enums.GearType gearType) {
        this.gearType = gearType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}