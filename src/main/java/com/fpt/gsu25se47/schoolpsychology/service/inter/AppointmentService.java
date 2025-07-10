package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewAppointment;

import java.util.Optional;

public interface AppointmentService {
    Optional<?> createAppointment(AddNewAppointment request);

    Optional<?> showHistoryAppointment();

    Optional<?> showAllAppointmentsOfSlots();

    Optional<?> updateAppointmentStatus(Integer appointmentId);

    Optional<?> cancelAppointment(Integer AppointmentId, String reasonCancel);


}
