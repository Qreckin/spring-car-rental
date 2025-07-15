package com.example.car.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotEmptyIfPresentValidator implements ConstraintValidator<NotEmptyIfPresent, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || !value.trim().isEmpty(); // null is allowed, "" is not
    }
}