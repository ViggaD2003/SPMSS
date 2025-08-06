package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.Teacher;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.AlertSkippedSurveyResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.CaseSummDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.CaseSummaryResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.OverviewResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TeacherDashboardResponse {

    private OverviewResponse overview;
    private List<CaseSummaryResponse> caseSummary;
    private List<CaseSummDetailResponse> cases;
    private AlertSkippedSurveyResponse alertSkippedSurveys;
}
