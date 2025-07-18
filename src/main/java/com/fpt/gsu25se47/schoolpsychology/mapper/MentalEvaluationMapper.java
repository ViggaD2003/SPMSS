package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateMentalEvaluationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.MentalEvaluationResponse;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.EvaluationType;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {StudentMapper.class})
public interface MentalEvaluationMapper {

    @Mapping(target = "id", ignore = true)
    @BeanMapping(builder = @Builder(disableBuilder = true))
    MentalEvaluation mapToMentalEvaluation(CreateMentalEvaluationRequest request,
                                           @Context Student student,
                                           @Context Category category,
                                           @Context ProgramRecord programRecord,
                                           @Context AppointmentRecord appointmentRecord,
                                           @Context SurveyRecord surveyRecord
                                           );

    @Mapping(target = "surveyRecordId", source = "surveyRecord.id")
    @Mapping(target = "appointmentRecordId", source = "appointmentRecord.id")
    @Mapping(target = "programRecordId", source = "programRecord.id")
    @Mapping(target = "student", source = "student", qualifiedByName = "mapStudentWithoutEvaluations")
    MentalEvaluationResponse mapToEvaluationResponse(MentalEvaluation mentalEvaluation);

    @AfterMapping
    default void setLok(@MappingTarget MentalEvaluation mentalEvaluation,
                                              @Context Student student,
                                              @Context Category category,
                                              @Context ProgramRecord programRecord,
                                              @Context AppointmentRecord appointmentRecord,
                                              @Context SurveyRecord surveyRecord) {

        if (programRecord != null) {
            mentalEvaluation.setEvaluationType(EvaluationType.PROGRAM);
            mentalEvaluation.setProgramRecord(programRecord);
        } else if (appointmentRecord != null) {
            mentalEvaluation.setEvaluationType(EvaluationType.APPOINTMENT);
            mentalEvaluation.setAppointmentRecord(appointmentRecord);
        } else if (surveyRecord != null) {
            mentalEvaluation.setEvaluationType(EvaluationType.SURVEY);
            mentalEvaluation.setSurveyRecord(surveyRecord);
        }

        mentalEvaluation.setStudent(student);
        mentalEvaluation.setCategory(category);
    }
}
