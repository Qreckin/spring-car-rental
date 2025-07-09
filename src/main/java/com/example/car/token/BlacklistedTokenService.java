package com.example.car.token;

import org.springframework.stereotype.Service;

@Service
public class BlacklistedTokenService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public BlacklistedTokenService(BlacklistedTokenRepository blacklistedTokenRepository){
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    public void blacklistToken(String token){
        if (isTokenBlacklisted(token)){
            throw new IllegalArgumentException("Token already expired");
        }
        blacklistedTokenRepository.save(new BlacklistedToken(token));
    }

    public boolean isTokenBlacklisted(String token){
        return blacklistedTokenRepository.findByToken(token).isPresent();
    }
}
