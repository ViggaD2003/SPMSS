package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class SurveyRecordDetailResponse {
    private Integer id;
    private Float totalScore;
    private Boolean isSkipped;
    private SurveyRecordType surveyRecordType;
    private LevelResponse level;
    private LocalDate completedAt;
    private SurveyGetAllResponse survey;
    private List<AnswerRecordResponse> answerRecords;
}
