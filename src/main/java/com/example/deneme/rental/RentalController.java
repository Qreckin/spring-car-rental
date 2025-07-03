package com.example.deneme.rental;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
public class RentalController {
    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/rentals")
    public List<Rental> filterRentals(
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) UUID carId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        return rentalService.filterRentals(customerId, carId, status, startDate, endDate);
    }

    @PostMapping("/rentals")
    public ResponseEntity<String> addRental(@Valid @RequestBody RentalRequestDTO rentalRequestDTO){
        rentalService.addRental(rentalRequestDTO);
        return ResponseEntity.ok("Rental has been created successfully.");
    }
}
