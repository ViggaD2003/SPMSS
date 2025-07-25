package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class SurveyGetAllResponse {

    private Integer surveyId;

    private String title;

    private String description;

    private Boolean isRequired;

    private Boolean isRecurring;

    private String recurringCycle;

    private String surveyType;

    private String status;

    private String targetScope;

    private String targetGrade;

    private LocalDate startDate;

    private LocalDate endDate;

    private CategoryResponse category;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer createdBy;
}
