package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewAppointment;
import com.fpt.gsu25se47.schoolpsychology.dto.request.ConfirmAppointment;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
@Tag(name = "Appointment API", description = "Xử lý đặt lịch, xem lịch sử và lịch dạy")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/show-history")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Lịch sử hẹn của sinh viên", description = "Trả về tất cả các cuộc hẹn mà sinh viên đã từng tham gia hoặc đặt.")
    public ResponseEntity<?> showAppointmentHistory() {
        return ResponseEntity.ok(appointmentService.showHistoryAppointment());
    }

    @GetMapping("/show-appointment")
    @PreAuthorize("hasRole('TEACHER') or hasRole('COUNSELOR')")
    @Operation(summary = "Xem các cuộc hẹn của slot", description = "Dành cho giáo viên hoặc cố vấn - hiển thị các appointment thuộc slot mà họ tổ chức.")
    public ResponseEntity<?> showAppointments() {
        return ResponseEntity.ok(appointmentService.showAllAppointmentsOfSlots());
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('PARENTS')")
    @PostMapping
    @Operation(summary = "Đặt cuộc hẹn mới", description = "Tạo một appointment mới giữa người đặt và người được đặt theo slot nhất định.")
    public ResponseEntity<?> addAppointment(@RequestBody AddNewAppointment request) {
        return ResponseEntity.ok(appointmentService.createAppointment(request));
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('PARENTS')")
    @PatchMapping("/cancel/{appointmentId}")
    @Operation(summary = "Huỷ cuộc hẹn", description = "Huỷ một appointment vì một lý do nào đó, lý do bắt buộc phải có")
    public ResponseEntity<?> cancelAppointment(@PathVariable("appointmentId") Integer id, @RequestParam(value = "reasonCancel") String reasonCancel) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id, reasonCancel));
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('COUNSELOR')")
    @PatchMapping
    @Operation(summary = "Xác nhận yêu cầu", description = "Xác nhận 1 yêu cầu mới từ appointment mới tạo của học sinh")
    public ResponseEntity<?> updateStatusAppointment(@Valid @RequestBody ConfirmAppointment request) {
        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(request));
    }


}
