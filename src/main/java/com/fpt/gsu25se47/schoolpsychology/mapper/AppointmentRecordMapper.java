package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAppointmentRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AppointmentRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.AppointmentRecord;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = AppointmentMapper.class)
public interface AppointmentRecordMapper {

    @Mapping(target = "status", expression = "java(appointmentRecord.getStatus().name())")
    @Mapping(target = "studentCoopLevel", expression = "java(appointmentRecord.getStudentCoopLevel() != null ? appointmentRecord.getStudentCoopLevel().name() : \"UNKNOWN\")")
    @Mapping(target = "sessionFlow", expression = "java(appointmentRecord.getSessionFlow() != null ? appointmentRecord.getSessionFlow().name() : \"UNKNOWN\")")
    @Mapping(target = "appointment", source = "appointment")
    AppointmentRecordResponse toAppointmentRecordResponse(AppointmentRecord appointmentRecord);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "answerRecords", ignore = true)
    @Mapping(target = "status", source = "request.status")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    AppointmentRecord toAppointmentRecord(CreateAppointmentRecordRequest request,
                                           @Context Appointment appointment);

    @AfterMapping
    default void setAppointment(@MappingTarget AppointmentRecord record,
                                 @Context Appointment appointment) {
        record.setAppointment(appointment);
        System.out.println("appointment set with ID: " + appointment.getId());
    }
}
