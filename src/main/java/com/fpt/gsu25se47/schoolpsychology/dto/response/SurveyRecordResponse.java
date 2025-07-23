package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class SurveyRecordResponse {

    private Integer id;
    private String noteSuggest;
    private BigDecimal totalScore;
    private String status;
    private String level;
    private LocalDate completedAt;
    private SurveyDetailResponse survey;
    private StudentDto studentDto;
    private List<AnswerRecordResponse> answerRecords;
}
