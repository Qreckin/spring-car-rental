package com.example.car.auth.security;

import com.example.car.CustomResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        CustomResponseEntity errorResponse = new CustomResponseEntity(
                CustomResponseEntity.UNAUTHORIZED,
                "Invalid or expired JWT token"
        );

        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }
}