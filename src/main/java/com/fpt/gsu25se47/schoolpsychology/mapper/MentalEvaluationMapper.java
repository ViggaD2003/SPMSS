package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateMentalEvaluationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.MentalEvaluationResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.MentalEvaluation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MentalEvaluationMapper {

    MentalEvaluation toMentalEvaluation(CreateMentalEvaluationRequest request);

    MentalEvaluationResponse toMentalEvaluationResponse(MentalEvaluation mentalEvaluation);

    @Mapping(target = "source", constant = "APPOINTMENT")
    @Mapping(target = "firstEvaluatedAt", expression = "java(appointment.getStartDateTime().toLocalDate())")
    @Mapping(target = "lastEvaluatedAt", expression = "java(appointment.getEndDateTime().toLocalDate())")
    @Mapping(target = "studentId",
            expression = "java(appointment.getBookedFor().getStudent() != null ? appointment.getBookedFor().getStudent().getId() : appointment.getBookedBy().getStudent().getId())")
    @Mapping(target = "appointmentId", source = "appointment.id")
    @Mapping(target = "sourceType", constant = "NONE")
    CreateMentalEvaluationRequest fromAppointment(Appointment appointment);
}
