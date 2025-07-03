package com.example.deneme.car;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CarRequestDTO {

    @NotBlank(message = "Make must not be blank")
    private String make; // Make of the car
    @NotBlank(message = "Model must not be blank")
    private String model; // Model of the car

    @NotNull(message = "Year must not be null")
    private Integer year; // Year of the car

    public CarRequestDTO(){

    }

    public CarRequestDTO(String make, String model, Integer year){
        this.make = make;
        this.model = model;
        this.year = year;
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
}
