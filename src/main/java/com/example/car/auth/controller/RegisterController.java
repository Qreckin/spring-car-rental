package com.example.car.auth.controller;

import com.example.car.auth.service.JwtService;
import com.example.car.auth.dto.RegisterRequest;
import com.example.car.customer.Customer;
import com.example.car.customer.CustomerRepository;
import com.example.car.CustomResponseEntity;
import com.example.car.user.User;
import com.example.car.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    private final UserService userService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(UserService userService, CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomResponseEntity> register(@Valid @RequestBody RegisterRequest request) {

        // Username check (only active users)
        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "Username is taken"));
        }

        // Email check (only active customers)
        if (customerRepository.existsByEmailAndNotDeleted(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "Email is taken"));
        }

        // Create new customer
        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setEmail(request.getEmail());
        customer.setBirthDate(request.getBirthDate());
        customer.setLicenseDate(request.getLicenseDate());

        // Save user with encoded password and customer
        User user = userService.saveUser(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                "USER",
                customer
        );

        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, "Customer with ID: " + customer.getId() + " has been created successfully"));
    }
}
