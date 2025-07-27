package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAppointmentRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateAppointmentRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AppointmentResponse;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    ResponseEntity<AppointmentResponse> createAppointment(@RequestBody @Valid CreateAppointmentRequest request) {

        return ResponseEntity.ok(appointmentService.createAppointment(request));
    }

    @GetMapping("/show-history")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Lịch sử hẹn của sinh viên", description = "Trả về tất cả các cuộc hẹn mà sinh viên đã từng tham gia hoặc đặt.")
    ResponseEntity<List<AppointmentResponse>> showAppointmentHistory() {

        return ResponseEntity.ok(appointmentService.getAppointmentsHistory());
    }

    @GetMapping("/show-appointment")
    @PreAuthorize("hasRole('TEACHER') or hasRole('COUNSELOR')")
    @Operation(summary = "Xem các cuộc hẹn của slot", description = "Dành cho giáo viên hoặc cố vấn - hiển thị các appointment thuộc slot mà họ tổ chức.")
    ResponseEntity<List<AppointmentResponse>> showAppointments() {

        return ResponseEntity.ok(appointmentService.getAllAppointmentsOfSlots());
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('PARENTS')")
    @PatchMapping("/cancel/{appointmentId}")
    @Operation(summary = "Huỷ cuộc hẹn", description = "Huỷ một appointment vì một lý do nào đó, lý do bắt buộc phải có")
    ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable("appointmentId") Integer id, @RequestParam(value = "reasonCancel") String reasonCancel) {

        return ResponseEntity.ok(appointmentService.cancelAppointment(id, reasonCancel));
    }

    @PatchMapping("/{appointmentId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('COUNSELOR')")
    ResponseEntity<AppointmentResponse> updateAppointment(@PathVariable Integer appointmentId, @RequestBody UpdateAppointmentRequest request) {

        return ResponseEntity.ok(appointmentService.updateAppointment(appointmentId, request));
    }

    @PatchMapping("/{appointmentId}/status")
    @PreAuthorize("hasRole('TEACHER') or hasRole('COUNSELOR')")
    ResponseEntity<AppointmentResponse> updateStatus(@PathVariable Integer appointmentId, @RequestParam AppointmentStatus status) {

        return ResponseEntity.ok(appointmentService.updateStatus(appointmentId, status));
    }

    @GetMapping("/status")
    ResponseEntity<List<AppointmentResponse>> getAllByStatus(@RequestParam AppointmentStatus status) {

        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status));
    }

    @GetMapping("/{appointmentId}")
    ResponseEntity<AppointmentResponse> getById(@PathVariable Integer appointmentId) {

        return ResponseEntity.ok(appointmentService.getAppointmentById(appointmentId));
    }
}