package com.example.deneme.auth;

import com.example.deneme.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

// JWT refers to JSON Web Tokens
// We need this class to generate tokens that securely validate user identities
// Secret key is assigned to the server, so each user has the same secret key
@Component // Used to tell Spring this class is a Bean
public class JwtService {
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    private final SecretKey secretKey; // Secret key that is generated from the secret

    public JwtService(@Value("${jwt.secret}") String secret){
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes()); // Creates the secret key from the given secret
    }


    public String generateToken(User user) {
        String subject;

        subject = user.getUsername();

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
