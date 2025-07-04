package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewAppointment;
import com.fpt.gsu25se47.schoolpsychology.dto.request.ConfirmAppointment;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;

import java.util.Optional;

public interface AppointmentService {
    Optional<?> createAppointment(AddNewAppointment request);

    Optional<?> showHistoryAppointment();

    Optional<?> showAllAppointmentsOfSlots();

    Optional<?> updateAppointmentStatus(ConfirmAppointment request);
}
