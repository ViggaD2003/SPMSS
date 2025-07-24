package com.fpt.gsu25se47.schoolpsychology.dto.request;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CreateSurveyRecordDto {

    private Boolean isSkipped;

    private Integer round;

    private Integer surveyId;

    private Float totalScore;

    private List<SubmitAnswerRecordRequest> answerRecordRequests;

}
