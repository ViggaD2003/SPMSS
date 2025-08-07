package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OverviewManager {
    private int totalStudents;

    private int totalPrograms;

    private int totalSurveys;

    private int totalAppointments;

    private int activeCases;
}
