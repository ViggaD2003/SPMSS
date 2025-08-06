package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OverviewResponse {

    private Integer totalStudents;
    private Integer studentsWithCases;
    private Integer studentsInPrograms;
    private Integer studentsCompletedSurveys;
}
