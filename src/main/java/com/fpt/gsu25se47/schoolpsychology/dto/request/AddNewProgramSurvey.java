package com.fpt.gsu25se47.schoolpsychology.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.Valid;
import java.util.List;

public record AddNewProgramSurvey(
        @NotBlank(message = "Survey type must not be blank")
        String surveyType,

        @NotEmpty(message = "Question list must not be empty")
        @Valid
        List<AddNewQuestionDto> questionDtos
) {
}
