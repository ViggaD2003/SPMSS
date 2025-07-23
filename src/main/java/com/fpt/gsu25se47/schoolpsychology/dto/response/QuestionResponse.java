package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class QuestionResponse {
    private Integer questionId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String description;

    private boolean isActive;

    private boolean isRequired;

    private String questionType;

    private String text;

    private List<AnswerResponse> answers;
}
