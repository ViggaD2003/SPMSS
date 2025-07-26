package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    private List<GradeDto> targetGrade;

    private LocalDate startDate;

    private LocalDate endDate;

    private CategoryResponse category;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private AccountDto createdBy;
}
