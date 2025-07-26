package com.fpt.gsu25se47.schoolpsychology.dto.request;

import lombok.Data;

import jakarta.validation.constraints.*;

@Data
public class CreateAssessmentScoreRequest {

    @NotNull(message = "Category ID must not be null")
    @Positive(message = "Category ID must be a positive number")
    private Integer categoryId;

    @NotNull(message = "Severity score must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Severity score must be >= 0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Severity score must be <= 5")
    private Float severityScore;

    @NotNull(message = "Frequency score must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Frequency score must be >= 0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Frequency score must be <= 5")
    private Float frequencyScore;

    @NotNull(message = "Impairment score must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Impairment score must be >= 0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Impairment score must be <= 5")
    private Float impairmentScore;

    @NotNull(message = "Chronicity score must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Chronicity score must be >= 0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Chronicity score must be <= 5")
    private Float chronicityScore;
}
