package com.example.jpa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ColorConverter implements AttributeConverter<Color, String> {
    @Override
    public String convertToDatabaseColumn(Color color) {
        return color.name().toLowerCase();
    }

    @Override
    public Color convertToEntityAttribute(String dbData) {
        return Color.valueOf(dbData);
    }
}

