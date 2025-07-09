package com.example.car.token;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "blacklisted_tokens")
public class BlacklistedToken {

    @Id
    private String token;

    private Instant expiration;

    public BlacklistedToken() {}

    public BlacklistedToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}