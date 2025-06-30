package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordLevel;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordStatus;
import lombok.Data;

import java.util.List;

@Data
public class CreateSurveyRecordDto {

    private String noteSuggest;
    private SurveyRecordStatus status;
    private Integer surveyId;
    private Integer totalScore;
    private SurveyRecordLevel level;
    private List<SubmitAnswerRecordRequest> answerRecordRequests;
}
