package com.fpt.gsu25se47.schoolpsychology.dto.request;


import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CreateSurveyRecordDto {

    private Boolean isSkipped;

    private Integer surveyId;

    private Float totalScore;

    private String surveyRecordType;

    private List<SubmitAnswerRecordRequest> answerRecordRequests;

}
