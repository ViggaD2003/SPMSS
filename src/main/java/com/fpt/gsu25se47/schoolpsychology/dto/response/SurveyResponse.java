package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SurveyResponse {

    private Integer surveyId;

    private String name;

    private String description;

    private Boolean isRequired;

    private Boolean isRecurring;

    private String recurringCycle;

    private LocalDate startDate;

    private String surveyCode;

    private LocalDate endDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String status;

    private List<QuestionResponse> questions;
}
