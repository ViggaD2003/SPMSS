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

    @PatchMapping("/cancel/{appointmentId}")
    @Operation(summary = "Huỷ cuộc hẹn", description = "Huỷ một appointment vì một lý do nào đó, lý do bắt buộc phải có")
    ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable("appointmentId") Integer id, @RequestParam(value = "reasonCancel") String reasonCancel) {

        return ResponseEntity.ok(appointmentService.cancelAppointment(id, reasonCancel));
    }

    @Operation(
            summary = "Update appointment",
            description = """
                    Role: TEACHER, COUNSELOR, MANAGER
                    For TEACHER:
                        Ignore assessmentScores, can be null or not null, the system will ignore it
                    For COUNSELOR:
                        Must have assessmentScores
                    """)
    @PatchMapping("/{appointmentId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('COUNSELOR') or hasRole('MANAGER')")
    ResponseEntity<AppointmentResponse> updateAppointment(@PathVariable Integer appointmentId, @RequestBody UpdateAppointmentRequest request) {

        return ResponseEntity.ok(appointmentService.updateAppointment(appointmentId, request));
    }

    @Operation(summary = "Update status of appointment",
            description = "Role: ALL")
    @PatchMapping("/{appointmentId}/status")
    ResponseEntity<AppointmentResponse> updateStatus(@PathVariable Integer appointmentId, @RequestParam AppointmentStatus status) {

        return ResponseEntity.ok(appointmentService.updateStatus(appointmentId, status));
    }

    @Operation(summary = "Get all appointments by status",
            description = "Role: MANAGER")
    @GetMapping("/status")
    @PreAuthorize("hasRole('MANAGER')")
    ResponseEntity<List<AppointmentResponse>> getAllByStatus(@RequestParam AppointmentStatus status) {

        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status).reversed());
    }

    @Operation(summary = "Get appointment details by ID",
            description = "Role: ALL")
    @GetMapping("/{appointmentId}")
    ResponseEntity<AppointmentResponse> getById(@PathVariable Integer appointmentId) {

        return ResponseEntity.ok(appointmentService.getAppointmentById(appointmentId));
    }

    @Operation(summary = "Get all appointments by accountId with status : CONFIRMED,IN_PROGRESS")
    @GetMapping("/account/{accountId}/active")
    ResponseEntity<List<AppointmentResponse>> findAllActiveAppointments(@PathVariable Integer accountId) {

        return ResponseEntity.ok(appointmentService.getAllAccAppointmentsByStatuses(accountId, List.of(
                AppointmentStatus.IN_PROGRESS,
                AppointmentStatus.CONFIRMED,
                AppointmentStatus.PENDING
        )).reversed());
    }

    @Operation(summary = "Get all appointments by accountId with status : CANCELED, COMPLETED, ABSENT")
    @GetMapping("/account/{accountId}/past")
    ResponseEntity<List<AppointmentResponse>> findAllPastAppointments(@PathVariable Integer accountId) {

        return ResponseEntity.ok(appointmentService.getAllAccAppointmentsByStatuses(accountId, List.of(
                AppointmentStatus.ABSENT,
                AppointmentStatus.CANCELED,
                AppointmentStatus.COMPLETED
        )).reversed());
    }
}