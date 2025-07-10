package com.example.car;

import com.example.car.customer.CustomerService;
import com.example.car.rental.RentalRepository;
import com.example.car.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("authService")
public class AuthService {

    private final CustomerService customerService;

    private final RentalRepository rentalRepository;

    public AuthService(CustomerService customerService, RentalRepository rentalRepository) {
        this.customerService = customerService;
        this.rentalRepository = rentalRepository;
    }

    public boolean isOwnerOrAdmin(UUID customerId, Authentication authentication) {
        boolean isAdmin = false;
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                isAdmin = true;
                break;
            }
        }

        if (isAdmin) {
            return true;
        }

        // 2. If not admin, check if user is the owner
        User user = (User)authentication.getPrincipal(); // typically the email or username
        String username = user.getUsername();

        return customerService.getCustomerById(customerId).getUser().getUsername().equals(username);
    }

    public boolean canCancelRental(UUID rentalID, Authentication authentication){
        boolean isAdmin = false;
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                isAdmin = true;
                break;
            }
        }

        if (isAdmin) {
            return true;
        }

        User user = (User) authentication.getPrincipal();
        return rentalRepository.isRentalOwnedByUser(rentalID, user.getUsername());
    }


}