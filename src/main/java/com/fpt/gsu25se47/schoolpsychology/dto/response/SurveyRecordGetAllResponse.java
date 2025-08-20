package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordIdentify;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class SurveyRecordGetAllResponse {
    private Integer id;
    private Float totalScore;
    private Boolean isSkipped;
    private SurveyRecordIdentify identify;
    private LevelResponse level;
    private LocalDateTime completedAt;
    private SurveyGetAllResponse survey;
}
