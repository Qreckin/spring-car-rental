package com.example.car.enums;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToStatusConverter implements Converter<String, Enums.Status> {

    @Override
    public Enums.Status convert(String source) {
        try {
            int value = Integer.parseInt(source);
            return Enums.Status.fromValue(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid status value: " + source);
        }
    }
}