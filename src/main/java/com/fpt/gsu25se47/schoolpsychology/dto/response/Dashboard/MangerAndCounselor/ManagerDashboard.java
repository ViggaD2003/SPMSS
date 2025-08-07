package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor;

import com.fpt.gsu25se47.schoolpsychology.dto.response.CaseGetAllResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ManagerDashboard {

    private OverviewManager overview;

    private List<ActivityByCategory> activityByCategories;

    private List<SurveyLevelByCategory> surveyLevelByCategories;

    private List<CaseGetAllResponse> activeCasesList;
}
