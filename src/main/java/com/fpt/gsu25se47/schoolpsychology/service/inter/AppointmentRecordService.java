package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAppointmentRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateAppointmentRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AppointmentRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppointmentRecordService {
    AppointmentRecordResponse createAppointmentRecord(CreateAppointmentRecordRequest createAppointmentRecordRequest);
    AppointmentRecordResponse getAppointmentRecordById(int appointmentRecordId);
    Page<AppointmentRecordResponse> getAllAppointmentRecords(Pageable pageable);
    Page<AppointmentRecordResponse> getAppointmentRecordsByField(AppointmentRole field, int accountId, Pageable pageable);
    AppointmentRecordResponse updateAppointmentRecord(Integer appointmentRecordId, UpdateAppointmentRecordRequest request);
}
