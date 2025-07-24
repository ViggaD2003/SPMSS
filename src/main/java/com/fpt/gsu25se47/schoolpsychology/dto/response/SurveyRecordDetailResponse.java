package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class SurveyRecordDetailResponse {
    private Integer id;
    private Float totalScore;
    private Integer round;
    private Boolean isSkipped;
    private LevelResponse level;
    private LocalDate completedAt;
    private SurveyDetailResponse survey;
    private List<AnswerRecordResponse> answerRecords;
}
