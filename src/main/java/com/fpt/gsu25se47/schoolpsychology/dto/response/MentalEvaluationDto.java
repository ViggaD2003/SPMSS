package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MentalEvaluationDto {

    private Integer id;

    private Integer evaluationRecordId;

    private String evaluationType;

    private Integer totalScore;

    private LocalDate date;

    private CategoryResponse categoryResponse;
}
