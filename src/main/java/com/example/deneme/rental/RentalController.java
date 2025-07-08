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
    public List<RentalDTO> filterRentals(
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
    public ResponseEntity<String> activateRental(@PathVariable UUID id,
                                                 @RequestParam LocalDateTime currentTime){
        rentalService.activateRental(id, currentTime);
        return ResponseEntity.ok("Rental marked as ACTIVE");
    }


    // COMPLETE a rental
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rentals/complete/{id}")
    public ResponseEntity<Map<String, Object>> completeRental(@PathVariable UUID id,
                                                 @RequestParam Integer newKilometer,
                                                 @RequestParam LocalDateTime currentTime){
        double totalPricePaid = rentalService.completeRental(id, newKilometer, currentTime);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Rental marked as COMPLETED");
        response.put("totalPricePaid", totalPricePaid);

        return ResponseEntity.ok(response);
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
