package com.example.car.rental.DTO;
import com.example.car.validation.NotEmptyIfPresent;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class RentalUpdateDTO {

    @Future(message = "New rental start date must be in the future")
    @NotNull
    private LocalDateTime rentalStartDate;
    @Future(message = "New rental end date must be in the future")
    @NotNull
    private LocalDateTime rentalEndDate;


    public RentalUpdateDTO(){

    }

    public RentalUpdateDTO(LocalDateTime rentalStartDate, LocalDateTime rentalEndDate) {
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
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
