package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StudentSkippedSurveyResponse {

    private String name;
    private List<String> skippedSurveys;
}
