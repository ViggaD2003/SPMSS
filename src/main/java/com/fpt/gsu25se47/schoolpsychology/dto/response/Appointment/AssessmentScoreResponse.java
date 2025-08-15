package com.fpt.gsu25se47.schoolpsychology.dto.response.Appointment;

import lombok.Data;

@Data
public class AssessmentScoreResponse {

    private Integer id;

    private Integer appointmentId;

    private Integer categoryId;

    private String categoryName;

    private String categoryCode;

    private Float severityScore;

    private Float frequencyScore;

    private Float impairmentScore;

    private Float chronicityScore;

    private Float compositeScore;
}