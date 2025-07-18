package com.fpt.gsu25se47.schoolpsychology.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class CreateMentalEvaluationRequest {

    private Integer appointmentRecordId;

    private Integer programRecordId;

    private Integer surveyRecordId;

    @NotNull
    private BigDecimal totalScore;

    @NotNull
    private LocalDate date;

    @NotNull
    private Integer categoryId;

    @NotNull
    private Integer studentId;
}
