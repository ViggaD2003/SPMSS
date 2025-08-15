package com.fpt.gsu25se47.schoolpsychology.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTermRequest {

    @NotNull(message = "Term number is required")
    @Min(value = 1, message = "Term number must be at least 1")
    private Integer termNumber;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;
}