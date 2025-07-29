package com.fpt.gsu25se47.schoolpsychology.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateSurveyRecordDto {

    @NotNull(message = "isSkipped must not be null")
    private Boolean isSkipped;

    @NotNull(message = "surveyId must not be null")
    @Positive(message = "surveyId must be positive")
    private Integer surveyId;

    @NotNull(message = "totalScore must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "totalScore must be >= 0")
    private Float totalScore;

    private String surveyRecordType;

    @NotNull(message = "answerRecordRequests must not be null")
    @Size(min = 1, message = "There must be at least one answer record")
    @Valid
    private List<SubmitAnswerRecordRequest> answerRecordRequests;
}
