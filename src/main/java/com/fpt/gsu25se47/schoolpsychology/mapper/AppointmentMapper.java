package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.AppointmentResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "hostName", source = "appointment.hostType")
    @Mapping(target = "bookForName", source = "appointment.bookedFor.email")
    @Mapping(target = "bookByName", source = "appointment.bookedBy.email")
    AppointmentResponse mapToResponse(Appointment appointment);
}
