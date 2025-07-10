package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProgramSurveyResponse{
    private Integer id;
    private String surveyType;
    private List<QuestionResponse> questions;
}
