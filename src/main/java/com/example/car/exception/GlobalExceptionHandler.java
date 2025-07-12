package com.example.car.exception;

import com.example.car.CustomResponseEntity;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponseEntity> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Validation error: " + errorMsg));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        String message = ex.getClass().getSimpleName() + ": " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParseError(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Malformed JSON request: " + ex.getMostSpecificCause().getMessage());
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<CustomResponseEntity> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        String errorMsg = ex.getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Validation error: " + errorMsg));
    }
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, String>> handleJwtException(JwtException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid JWT token");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CustomResponseEntity> handleMissingParam(MissingServletRequestParameterException ex) {
        String paramName = ex.getParameterName();
        return ResponseEntity.badRequest().body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Missing required parameter: " + paramName));
    }

}
