package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard;

import lombok.Data;

import java.util.List;

@Data
public class AlertSkippedSurveyResponse {

    private Integer totalSkippedThisMonth;
    private List<StudentSkippedSurveyResponse> students;
}
