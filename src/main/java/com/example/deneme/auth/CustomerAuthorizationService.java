package com.example.deneme.auth;

import com.example.deneme.rental.Rental;
import com.example.deneme.rental.RentalRepository;
import com.example.deneme.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("authService")
public class CustomerAuthorizationService {
    @Autowired
    private RentalRepository rentalRepository;

    public boolean isOwnerOrAdmin(UUID customerId, Authentication authentication) {
        User user = (User) authentication.getPrincipal(); // Your User class
        return user.getRole().equals("ADMIN") || user.getCustomer().getId().equals(customerId);
    }


    public boolean isRentalOwnerOrAdmin(UUID rentalId, Authentication authentication) {
        Optional<Rental> rentalOptional = rentalRepository.findById(rentalId);
        if (rentalOptional.isEmpty()) return false;

        Rental rental = rentalOptional.get();
        UUID rentalCustomerId = rental.getCustomer().getId();

        User user = (User) authentication.getPrincipal();
        return user.getRole().equals("ADMIN") || user.getCustomer().getId().equals(rentalCustomerId);
    }


    public boolean canCancelRental(UUID rentalId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        // ADMIN can cancel any rental
        if (user.getRole().equals("ADMIN")) {
            return true;
        }

        // Check if rental exists and belongs to this user’s customer, and is in RESERVED status
        return rentalRepository.findById(rentalId)
                .filter(r -> r.getCustomer() != null)
                .filter(r -> r.getCustomer().getId().equals(user.getCustomer().getId()))
                .filter(r -> r.getStatus() == Rental.Status.RESERVED)
                .isPresent();
    }
}
