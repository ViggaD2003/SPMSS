package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAppointmentRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateAppointmentRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Appointment.AppointmentResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.BookedSlot;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, MentalEvaluationMapper.class, AssessmentScoreMapper.class, SlotMapper.class})
public interface AppointmentMapper {

    @Mapping(target = "id", ignore = true)
    Appointment toAppointment(CreateAppointmentRequest request);

    @Mapping(target = "updatedAt", source = "updatedDate")
    @Mapping(target = "createdAt", source = "createdDate")
    @Mapping(target = "hostedBy", source = "slot.hostedBy")
    AppointmentResponse toAppointmentResponse(Appointment appointment);

    @Mapping(target = "updatedAt", source = "updatedDate")
    @Mapping(target = "createdAt", source = "createdDate")
    @Mapping(target = "hostedBy", source = "slot.hostedBy")
    @Mapping(target = "assessmentScores", ignore = true)
    AppointmentResponse toAppointmentResponseWithoutAS(Appointment appointment);

    @Mapping(target = "assessmentScores", ignore = true)
    Appointment updateAppointmentFromRequest(UpdateAppointmentRequest request,
                                             @MappingTarget Appointment appointment);

    BookedSlot toBookedSlot(Appointment appointment);
}
