package com.example.car.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GearTypeConverter implements AttributeConverter<Enums.GearType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Enums.GearType gearType) {
        return gearType != null ? gearType.getValue() : null;
    }

    @Override
    public Enums.GearType convertToEntityAttribute(Integer value) {
        return value != null ? Enums.GearType.fromValue(value) : null;
    }
}