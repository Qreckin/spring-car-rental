package com.example.deneme.rental;

import com.example.deneme.rental.Rental;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Rental.Status, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Rental.Status status) {
        return status != null ? status.getCode() : null;
    }

    @Override
    public Rental.Status convertToEntityAttribute(Integer code) {
        return code != null ? Rental.Status.fromCode(code) : null;
    }
}