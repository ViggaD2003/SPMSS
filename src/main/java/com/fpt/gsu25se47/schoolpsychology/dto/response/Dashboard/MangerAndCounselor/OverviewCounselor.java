package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OverviewCounselor {

    private int myActiveCases;

    private int myAppointmentsThisMonth;

    private int mySurveysReviewed;

    private int programsReferred;
}
