package com.example.car.enums;

import com.example.car.enums.Enums;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToGearTypeConverter implements Converter<String, Enums.GearType> {

    @Override
    public Enums.GearType convert(String source) {
        try {
            int value = Integer.parseInt(source);
            return Enums.GearType.fromValue(value); // Your existing fromValue method
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("GearType must be 0 (AUTOMATIC) or 1 (MANUAL)");
        }
    }
}