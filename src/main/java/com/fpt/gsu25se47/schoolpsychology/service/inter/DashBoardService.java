package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor.CounselorDashboard;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor.ManagerDashboard;

import java.util.Optional;

public interface DashBoardService {
    ManagerDashboard managerDashboard();

    CounselorDashboard counselorDashboard();
}
