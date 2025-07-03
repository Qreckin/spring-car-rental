package com.example.deneme.rental;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RentalController {
    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/rentals")
    public List<Rental> getRentals(){
        return rentalService.getRentals();
    }

    @PostMapping("/rentals")
    public ResponseEntity<String> addRental(@RequestBody RentalRequestDTO rentalRequestDTO){
        rentalService.addRental(rentalRequestDTO);
        return ResponseEntity.ok("Rental has been created successfully.");
    }
}
