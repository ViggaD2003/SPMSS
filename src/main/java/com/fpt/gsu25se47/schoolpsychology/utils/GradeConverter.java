package com.fpt.gsu25se47.schoolpsychology.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class GradeConverter implements AttributeConverter<List<Grade>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Grade> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not convert list of grades to JSON", e);
        }
    }

    @Override
    public List<Grade> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<Grade>>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not convert JSON to list of grades", e);
        }
    }
}
