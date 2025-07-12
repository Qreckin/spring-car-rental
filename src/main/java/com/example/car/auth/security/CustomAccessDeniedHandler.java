package com.example.car.auth.security;

import com.example.car.CustomResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        CustomResponseEntity error = new CustomResponseEntity(
                CustomResponseEntity.FORBIDDEN,
                "You do not have permission to access this resource"
        );

        new ObjectMapper().writeValue(response.getWriter(), error);
    }
}