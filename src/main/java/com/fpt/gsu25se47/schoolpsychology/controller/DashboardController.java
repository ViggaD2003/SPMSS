package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor.CounselorDashboard;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor.ManagerDashboard;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.Teacher.TeacherDashboardResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.DashBoardService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.TeacherDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final TeacherDashboardService teacherDashboardService;

    private final DashBoardService dashBoardService;

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/teacher")
    ResponseEntity<TeacherDashboardResponse> getTeacherDashboard() {
        return ResponseEntity.ok(teacherDashboardService.getTeacherDashboardResponse());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manager")
    public ResponseEntity<ManagerDashboard> getManagerDashboard() {
        return ResponseEntity.ok(dashBoardService.managerDashboard());
    }


    @PreAuthorize("hasRole('COUNSELOR')")
    @GetMapping("/counselor")
    public ResponseEntity<CounselorDashboard> getCounselorDashboard() {
        return ResponseEntity.ok(dashBoardService.counselorDashboard());
    }
}
