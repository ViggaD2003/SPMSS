package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProgramSurveyDto {
    private Integer id;
    private String name;
    private String description;
    private String surveyType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
