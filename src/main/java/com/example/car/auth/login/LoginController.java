package com.example.car.auth.login;

import com.example.car.auth.JwtService;
import com.example.car.token.BlacklistedTokenService;
import com.example.car.user.User;
import com.example.car.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.AuthenticationException;

@RestController
public class LoginController {
    private final AuthenticationManager authenticationManager;

    private final BlacklistedTokenService blacklistedTokenService;

    private final UserService userService;
    private final JwtService jwtService;

    public LoginController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService, BlacklistedTokenService blacklistedTokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
        this.blacklistedTokenService = blacklistedTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
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
            return ResponseEntity.ok(new LoginResponse(token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication, @RequestHeader("Authorization") String authHeader) {
        User user = (User) authentication.getPrincipal();

        // Remove "Bearer " prefix
        String token = authHeader.replace("Bearer ", "");

        // Now you can blacklist it or log it
        blacklistedTokenService.blacklistToken(token);

        return ResponseEntity.ok("Logged out and token blacklisted.");
    }
}
