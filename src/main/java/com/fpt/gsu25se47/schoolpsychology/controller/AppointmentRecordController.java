package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.common.ApiResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAppointmentRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AppointmentRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentRole;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AppointmentRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment-records")
@RequiredArgsConstructor
public class AppointmentRecordController {

    private final AppointmentRecordService appointmentRecordService;

    @PostMapping
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<AppointmentRecordResponse>> createAppointmentRecord(@RequestBody CreateAppointmentRecordRequest request) {

        AppointmentRecordResponse appointmentRecordResponse = appointmentRecordService.createAppointmentRecord(request);

        return ResponseEntity.ok(ApiResponse.<AppointmentRecordResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Create appointment record successfully")
                .data(appointmentRecordResponse)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentRecordResponse>> getAppointmentRecordById(@PathVariable int id) {

        return ResponseEntity.ok(ApiResponse.<AppointmentRecordResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Retrieved Appointment Record successfully")
                .data(appointmentRecordService.getAppointmentRecordById(id))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentRecordResponse>>> getAll() {

        return ResponseEntity.ok(ApiResponse.<List<AppointmentRecordResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Retrieved all Appointment Records successfully")
                .data(appointmentRecordService.getAllAppointmentRecords())
                .build());
    }

        @GetMapping("/accounts")
        public ResponseEntity<ApiResponse<List<AppointmentRecordResponse>>> getAllByFieldAccountId(
                @RequestParam AppointmentRole field,
                @RequestParam int accountId
        ) {

            return ResponseEntity.ok(ApiResponse.<List<AppointmentRecordResponse>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .success(true)
                    .message("Retrieved all Appointment Records successfully")
                    .data(appointmentRecordService.getAppointmentRecordsByField(field, accountId))
                    .build());
        }
}
