package com.example.deneme.rental;

import com.example.deneme.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    // Retrieve non-deleted rentals

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/rentals")
    public List<Rental> filterRentals(
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) UUID carId,
            @RequestParam(required = false) Rental.Status status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        return rentalService.filterRentals(customerId, carId, status, startDate, endDate);
    }

    // Create a rental
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/rentals")
    public ResponseEntity<String> addRental(@Valid @RequestBody RentalRequestDTO rentalRequestDTO,
                                            Authentication authentication){
        User user = (User)authentication.getPrincipal();
        UUID customerId = user.getCustomer().getId();

        rentalService.addRental(rentalRequestDTO, customerId);
        return ResponseEntity.ok("Rental has been created successfully.");
    }

    // ACTIVATE a rental
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rentals/activate/{id}")
    public ResponseEntity<String> activateRental(@PathVariable UUID id){
        rentalService.activateRental(id);
        return ResponseEntity.ok("Rental marked as COMPLETED");
    }


    // COMPLETE a rental
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rentals/complete/{id}")
    public ResponseEntity<String> completeRental(@PathVariable UUID id, @RequestParam Integer newKilometer){
        rentalService.completeRental(id, newKilometer);
        return ResponseEntity.ok("Rental marked as COMPLETED");
    }


    // CANCELLING a rental
    @PreAuthorize("@authService.canCancelRental(#id, authentication)")
    @PostMapping("/rentals/cancel/{id}")
    public ResponseEntity<String> cancelRental(@PathVariable UUID id){
        rentalService.cancelRental(id);
        return ResponseEntity.ok("Rental marked as CANCELLED");
    }


    // Delete a rental
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/rentals/delete/{id}")
    public ResponseEntity<String> deleteRental(@PathVariable UUID id){
        rentalService.deleteRental(id);
        return ResponseEntity.ok("Rental has been deleted successfully.");
    }


}
