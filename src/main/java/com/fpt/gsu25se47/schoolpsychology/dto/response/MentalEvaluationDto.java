package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MentalEvaluationDto {

    private Integer id;

    private Integer evaluationRecordId;

    private String evaluationType;

    private BigDecimal totalScore;

    private LocalDate date;

//    private CategoryDetailResponse categoryResponse;
}
