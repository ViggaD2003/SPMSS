package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAppointmentRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AppointmentRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentRole;

import java.util.List;

public interface AppointmentRecordService {
    AppointmentRecordResponse createAppointmentRecord(CreateAppointmentRecordRequest createAppointmentRecordRequest);
    AppointmentRecordResponse getAppointmentRecordById(int appointmentRecordId);
    List<AppointmentRecordResponse> getAllAppointmentRecords();
    List<AppointmentRecordResponse> getAppointmentRecordsByField(AppointmentRole field, int accountId);
//    List<AppointmentRecordResponse> getAllAppointmentRecordsById();
}
