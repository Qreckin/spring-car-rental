package com.example.car.rental;

import com.example.car.CustomResponseEntity;
import com.example.car.enums.Enums;
import com.example.car.rental.DTO.RentalDTO;
import com.example.car.rental.DTO.RentalRequestDTO;
import com.example.car.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class RentalController {
    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    // Retrieve non-deleted rentals

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/rentals")
    public ResponseEntity<CustomResponseEntity> filterRentals(
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) UUID carId,
            @RequestParam(required = false) Enums.Status status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return rentalService.filterRentals(customerId, carId, status, startDate, endDate);
    }

    // Create a rental
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/rentals")
    public ResponseEntity<CustomResponseEntity> addRental(@Valid @RequestBody RentalRequestDTO rentalRequestDTO,
                                            Authentication authentication){
        User user = (User)authentication.getPrincipal();
        UUID customerId = user.getCustomer().getId();

        return rentalService.addRental(rentalRequestDTO, customerId);
    }

    // ACTIVATE a rental
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rentals/activate/{id}")
    public ResponseEntity<CustomResponseEntity> activateRental(@PathVariable UUID id,
                                                 @RequestParam LocalDateTime currentTime){
        return rentalService.activateRental(id, currentTime);
    }


    // COMPLETE a rental
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rentals/complete/{id}")
    public ResponseEntity<CustomResponseEntity> completeRental(@PathVariable UUID id,
                                                 @RequestParam Integer newKilometer,
                                                 @RequestParam LocalDateTime currentTime){
        return rentalService.completeRental(id, newKilometer, currentTime);
    }


    // CANCELLING a rental
    @PreAuthorize("@authService.canCancelRental(#id, authentication)")
    @PostMapping("/rentals/cancel/{id}")
    public ResponseEntity<CustomResponseEntity> cancelRental(@PathVariable UUID id){
        return rentalService.cancelRental(id);
    }


    // Delete a rental
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/rentals/delete/{id}")
    public ResponseEntity<CustomResponseEntity> deleteRental(@PathVariable UUID id){
        return rentalService.deleteRental(id);
    }


}
