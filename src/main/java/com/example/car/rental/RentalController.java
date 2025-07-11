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
        List<RentalDTO> result = rentalService.filterRentals(customerId, carId, status, startDate, endDate);
        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, result));
    }

    // Create a rental
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/rentals")
    public ResponseEntity<CustomResponseEntity> addRental(@Valid @RequestBody RentalRequestDTO rentalRequestDTO,
                                            Authentication authentication){
        User user = (User)authentication.getPrincipal();
        UUID customerId = user.getCustomer().getId();

        rentalService.addRental(rentalRequestDTO, customerId);
        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, "Rental has been created successfully."));
    }

    // ACTIVATE a rental
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rentals/activate/{id}")
    public ResponseEntity<CustomResponseEntity> activateRental(@PathVariable UUID id,
                                                 @RequestParam LocalDateTime currentTime){
        rentalService.activateRental(id, currentTime);
        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, "Rental has been activated successfully"));
    }


    // COMPLETE a rental
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rentals/complete/{id}")
    public ResponseEntity<CustomResponseEntity> completeRental(@PathVariable UUID id,
                                                 @RequestParam Integer newKilometer,
                                                 @RequestParam LocalDateTime currentTime){
        double totalPricePaid = rentalService.completeRental(id, newKilometer, currentTime);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Rental has been completed successfully");
        response.put("totalPricePaid", totalPricePaid);

        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, response));
    }


    // CANCELLING a rental
    @PreAuthorize("@authService.canCancelRental(#id, authentication)")
    @PostMapping("/rentals/cancel/{id}")
    public ResponseEntity<CustomResponseEntity> cancelRental(@PathVariable UUID id){
        rentalService.cancelRental(id);
        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, "Rental has been cancelled successfully"));
    }


    // Delete a rental
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/rentals/delete/{id}")
    public ResponseEntity<CustomResponseEntity> deleteRental(@PathVariable UUID id){
        rentalService.deleteRental(id);
        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, "Rental has been deleted successfully"));
    }


}
