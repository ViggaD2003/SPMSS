package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAppointmentRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateAppointmentRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AppointmentResponse;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse createAppointment(CreateAppointmentRequest request);
    List<AppointmentResponse> getAppointmentsHistory();
    List<AppointmentResponse> getAllAppointmentsOfSlots();
    AppointmentResponse cancelAppointment(Integer appointmentId, String reasonCancel);
    AppointmentResponse updateAppointment(Integer appointmentId, UpdateAppointmentRequest request);
    AppointmentResponse updateStatus(Integer appointmentId, AppointmentStatus status);
    AppointmentResponse getAppointmentById(Integer appointmentId);
    List<AppointmentResponse> getAppointmentsByStatus(AppointmentStatus appointmentStatus);
}