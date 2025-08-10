package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor.CounselorDashboard;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor.ManagerDashboard;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor.StudentDashboard;

import java.time.LocalDate;
import java.util.Optional;

public interface DashBoardService {
    ManagerDashboard managerDashboard();

    CounselorDashboard counselorDashboard();

    StudentDashboard studentDashboard(LocalDate startDate, LocalDate endDate, Integer studentId);
}
