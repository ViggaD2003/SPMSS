package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OverviewStudent {

    private int totalSurveys;

    private int totalPrograms;

    private int totalAppointments;

    private int totalCases;
}
