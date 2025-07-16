package com.example.car.auth.controller;

import com.example.car.auth.service.JwtService;
import com.example.car.auth.dto.RegisterRequest;
import com.example.car.customer.Customer;
import com.example.car.customer.CustomerRepository;
import com.example.car.CustomResponseEntity;
import com.example.car.customer.CustomerService;
import com.example.car.customer.DTO.CustomerDTO;
import com.example.car.enums.Enums;
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
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(UserService userService, CustomerService customerService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomResponseEntity> register(@Valid @RequestBody RegisterRequest request) {

        // Username check
        if (customerService.getCustomerByUsername(request.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "Username is taken"));
        }

        // Email check
        if (customerService.getCustomerByEmail(request.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "Email is taken"));
        }


        // Phone number check
        if (customerService.getCustomerByPhoneNumber(request.getPhoneNumber()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "Phone number is taken"));
        }


        // Create new customer
        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setEmail(request.getEmail());
        customer.setBirthDate(request.getBirthDate());
        customer.setLicenseDate(request.getLicenseDate());
        customer.setLicenseType(Enums.GearType.fromValue(request.getLicenseType()));

        // Save user with encoded password and customer
        User user = userService.saveUser(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                "USER",
                customer
        );

        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, "Customer has been created successfully"));
    }
}
