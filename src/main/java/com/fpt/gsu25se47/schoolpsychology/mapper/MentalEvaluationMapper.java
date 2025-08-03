package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateMentalEvaluationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.MentalEvaluationResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyStatic;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.MentalEvaluation;
import com.fpt.gsu25se47.schoolpsychology.model.SurveyRecord;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class MentalEvaluationMapper {



    public abstract MentalEvaluation toMentalEvaluation(CreateMentalEvaluationRequest request);

    public abstract MentalEvaluationResponse toMentalEvaluationResponse(MentalEvaluation mentalEvaluation);

    @Mapping(target = "source", constant = "APPOINTMENT")
    @Mapping(target = "latestEvaluatedAt", expression = "java(appointment.getUpdatedDate().toLocalDate())")
    @Mapping(target = "studentId",
            expression = "java(appointment.getBookedFor().getStudent() != null ? appointment.getBookedFor().getStudent().getId() : appointment.getBookedBy().getStudent().getId())")
    @Mapping(target = "appointmentId", source = "appointment.id")
    public abstract CreateMentalEvaluationRequest fromAppointment(Appointment appointment);

    @Mapping(target = "source", expression = "java(" +
            "switch (surveyRecord.getSurvey().getSurveyType()) {\n" +
            "                    case SCREENING -> Source.SURVEY;\n" +
            "                    case PROGRAM -> Source.PROGRAM;\n" +
            "                    case FOLLOWUP -> Source.APPOINTMENT;\n" +
            "                }" +
            " )")
    @Mapping(target = "latestEvaluatedAt", expression = "java(surveyRecord.getCompletedAt())")
    @Mapping(target = "studentId",
            expression = "java(surveyRecord.getStudent().getId())")
    @Mapping(target = "surveyRecordId", source = "surveyRecord.id")
    public abstract CreateMentalEvaluationRequest fromSurveyRecord(SurveyRecord surveyRecord);

}
