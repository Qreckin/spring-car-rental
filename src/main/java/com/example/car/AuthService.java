package com.example.car;

import com.example.car.customer.CustomerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("authService")
public class AuthService {

    private final CustomerService customerService;

    public AuthService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public boolean isOwnerOrAdmin(UUID customerId, Authentication authentication) {
        // 1. Check if user has ADMIN role
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (isAdmin) {
            return true;
        }

        // 2. If not admin, check if user is the owner
        String authenticatedEmail = authentication.getName(); // typically the email or username

        // You must have a way to get the customer's email from the ID
        String customerEmail = customerService.getCustomerById(customerId).getEmail();

        return authenticatedEmail.equalsIgnoreCase(customerEmail);
    }
}