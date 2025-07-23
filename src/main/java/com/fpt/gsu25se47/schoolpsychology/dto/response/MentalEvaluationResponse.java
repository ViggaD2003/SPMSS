package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MentalEvaluationResponse {
    private Integer id;

    private String evaluationType;

    private Integer evaluationRecordId;

    private BigDecimal totalScore;

    private LocalDate date;

    private CategoryDetailResponse category;

    private StudentDto student;
}
