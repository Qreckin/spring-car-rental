package com.example.car.enums;

import com.example.car.rental.Rental;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Enums.Status, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Enums.Status status) {
        return status != null ? status.getCode() : null;
    }

    @Override
    public Enums.Status convertToEntityAttribute(Integer code) {
        return code != null ? Enums.Status.fromCode(code) : null;
    }
}