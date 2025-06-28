package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.EvaluationType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateMentalEvaluationRequest {

    @NotNull
    private EvaluationType evaluationType;

    @NotNull
    private Integer evaluationRecordId;

    @NotNull
    private Integer totalScore;

    @NotNull
    private LocalDate date;

    @NotNull
    private Integer categoryId;

    @NotNull
    private Integer studentId;
}
