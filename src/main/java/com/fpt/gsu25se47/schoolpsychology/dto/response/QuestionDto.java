package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuestionDto {
    private Integer questionId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String description;

    private boolean isActive;

    private boolean isRequired;

    private String questionType;

    private String text;
}
