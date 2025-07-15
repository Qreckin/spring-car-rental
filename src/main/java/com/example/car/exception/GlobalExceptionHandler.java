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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // When validation fails on RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponseEntity> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        CustomResponseEntity response = new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // When you give malformed json format for ex: Date is 2025-13-35 this exception is thrown
    // Or when gear type is not matching
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomResponseEntity> handleJsonParseError(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Malformed JSON request: " + ex.getMostSpecificCause().getMessage()));
    }

    // When validation fails on RequestParam
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<CustomResponseEntity> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        String errorMsg = ex.getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Validation error: " + errorMsg));
    }

    // This exception is thrown when a required parameter is not given
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CustomResponseEntity> handleMissingParam(MissingServletRequestParameterException ex) {
        String paramName = ex.getParameterName();
        return ResponseEntity.badRequest().body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Missing required parameter: " + paramName));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomResponseEntity> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Invalid input: " + ex.getMessage()));
    }

}
