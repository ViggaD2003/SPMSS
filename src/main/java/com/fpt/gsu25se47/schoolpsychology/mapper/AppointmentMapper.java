package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.AppointmentResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponse mapToResponse(Appointment appointment) {

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .bookForName(appointment.getBookedFor().getEmail())
                .bookByName(appointment.getBookedBy().getEmail())
                .hostName(appointment.getHostType().name())
                .reason(appointment.getReason())
                .status(appointment.getStatus())
                .isOnline(appointment.getIsOnline())
                .location(appointment.getLocation())
                .startDateTime(appointment.getStartDateTime())
                .endDateTime(appointment.getEndDateTime())
                .build();
    }
}
