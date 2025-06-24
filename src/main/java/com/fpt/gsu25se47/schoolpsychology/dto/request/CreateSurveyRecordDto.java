package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyStatus;
import lombok.Data;

import java.util.List;

@Data
public class CreateSurveyRecordDto {

    private String noteSuggest;
    private SurveyStatus surveyStatus;
    private Integer surveyId;
    private Integer mentalEvaluationId;
    private List<Integer> answerRecordRequests;
}
