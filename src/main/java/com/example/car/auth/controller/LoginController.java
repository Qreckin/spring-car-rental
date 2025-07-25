package com.example.car.auth.controller;

import com.example.car.auth.service.JwtService;
import com.example.car.CustomResponseEntity;
import com.example.car.auth.dto.TokenResponse;
import com.example.car.auth.dto.LoginRequest;
import com.example.car.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.AuthenticationException;

@RestController
public class LoginController {
    private final AuthenticationManager authenticationManager;


    private final UserService userService;
    private final JwtService jwtService;

    public LoginController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponseEntity> login(@RequestBody LoginRequest request) {
        try {
            // Validation of username password pair
            // This works because UserDetailsServiceImpl implements UserDetailsService
            // And there is loadByUsername method
            // If not authenticated throws exception
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Generate and return the token as JSON
            String token = jwtService.generateToken(userService.getByUsername(request.getUsername()));
            return ResponseEntity.ok(CustomResponseEntity.OK(new TokenResponse(token)));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomResponseEntity.UNAUTHORIZED);
        }
    }
}
