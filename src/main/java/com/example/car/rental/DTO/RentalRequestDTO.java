package com.example.car.rental.DTO;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class RentalRequestDTO {

    @NotNull(message = "Car ID must not be null")
    private UUID carId;


    @NotNull(message = "Date must not be null")
    private LocalDateTime rentalStartDate;
    @NotNull(message = "Date must not be null")
    private LocalDateTime rentalEndDate;


    public RentalRequestDTO(){

    }

    public RentalRequestDTO(UUID carId, LocalDateTime rentalStartDate, LocalDateTime rentalEndDate) {
        this.carId = carId;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
    }


    public UUID getCarId() {
        return carId;
    }

    public void setCarId(UUID carId) {
        this.carId = carId;
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

}
