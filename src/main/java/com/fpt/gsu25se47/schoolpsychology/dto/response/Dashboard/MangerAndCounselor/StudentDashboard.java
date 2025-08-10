package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor;

import com.fpt.gsu25se47.schoolpsychology.dto.response.MentalEvaluationStatic;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentDashboard {

    private OverviewStudent overview;

    private MentalEvaluationStatic mentalStatistic;

}
