package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MentalEvaluationResponse {
    private Integer id;

    private String evaluationType;

    private Integer evaluationRecordId;

    private Integer totalScore;

    private LocalDate date;

    private CategoryResponse category;

    private StudentDto student;
}
