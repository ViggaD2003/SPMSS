package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor;

import com.fpt.gsu25se47.schoolpsychology.dto.response.CaseGetAllResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CounselorDashboard {

    private OverviewCounselor overview;

    private List<UpcomingAppointments> upcomingAppointments;

    private List<CaseByCategory> caseByCategory;

    private List<CaseGetAllResponse> acitveCaseList;

}
