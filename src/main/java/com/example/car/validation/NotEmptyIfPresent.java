package com.example.car.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotEmptyIfPresentValidator.class)
public @interface NotEmptyIfPresent {
    String message() default "Field must not be empty if provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}