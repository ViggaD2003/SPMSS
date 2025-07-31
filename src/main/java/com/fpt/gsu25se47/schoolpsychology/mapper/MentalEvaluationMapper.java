package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateMentalEvaluationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.MentalEvaluationResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.MentalEvaluation;
import com.fpt.gsu25se47.schoolpsychology.model.SurveyRecord;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MentalEvaluationMapper {

    MentalEvaluation toMentalEvaluation(CreateMentalEvaluationRequest request);

    MentalEvaluationResponse toMentalEvaluationResponse(MentalEvaluation mentalEvaluation);

    @Mapping(target = "source", constant = "APPOINTMENT")
    @Mapping(target = "latestEvaluatedAt", expression = "java(appointment.getUpdatedDate().toLocalDate())")
    @Mapping(target = "studentId",
            expression = "java(appointment.getBookedFor().getStudent() != null ? appointment.getBookedFor().getStudent().getId() : appointment.getBookedBy().getStudent().getId())")
    @Mapping(target = "appointmentId", source = "appointment.id")
    @Mapping(target = "sourceType", constant = "NONE")
    CreateMentalEvaluationRequest fromAppointment(Appointment appointment);

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
    @Mapping(target = "sourceType", expression = "java(surveyRecord.getSurveyRecordType().name().equals(\"EXIT\") || " +
            "surveyRecord.getSurveyRecordType().name().equals(\"ENTRY\") ? SourceType.valueOf(surveyRecord.getSurveyRecordType().name()) : SourceType.NONE)")
    CreateMentalEvaluationRequest fromSurveyRecord(SurveyRecord surveyRecord);

}
