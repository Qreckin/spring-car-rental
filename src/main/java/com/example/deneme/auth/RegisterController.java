package com.example.deneme.auth;

import com.example.deneme.customer.Customer;
import com.example.deneme.customer.CustomerRepository;
import com.example.deneme.customer.CustomerService;
import com.example.deneme.user.User;
import com.example.deneme.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RegisterController {

    private final UserService userService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public RegisterController(UserService userService, CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(new RegisterResponse("Username already exists", null, null));
        }

        if (customerRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new RegisterResponse("Email already in use", null, null));
        }

        // Create customer
        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setEmail(request.getEmail());
        customer.setBirthDate(request.getBirthDate());
        customer.setLicenseYear(request.getLicenseYear());

        // Save user and link customer
        User user = userService.saveUser(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                "USER",
                customer
        );


        // Generate token for login
        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(
                new RegisterResponse("User registered successfully", customer.getId(), token)
        );
    }
}
