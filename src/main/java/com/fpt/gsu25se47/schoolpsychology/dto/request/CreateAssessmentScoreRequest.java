package com.fpt.gsu25se47.schoolpsychology.dto.request;

import lombok.Data;

@Data
public class CreateAssessmentScoreRequest {
    private Integer categoryId;
    private Float severityScore;
    private Float frequencyScore;
    private Float impairmentScore;
    private Float chronicityScore;
}
