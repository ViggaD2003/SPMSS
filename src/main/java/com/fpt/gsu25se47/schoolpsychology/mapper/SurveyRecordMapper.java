package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring",
        imports = {LocalDate.class},
        uses = {SurveyMapper.class,
                AnswerRecordMapper.class,
                ClassMapper.class,
                StudentMapper.class,
                SurveyMapper.class})
public interface SurveyRecordMapper {

    @Mapping(target = "survey", source = "survey")
    @Mapping(target = "completedAt", expression = "java(LocalDate.now())")
    @Mapping(target = "answerRecords", source = "answerRecords")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", source = "account")
    @Mapping(target = "status", source = "dto.status")
    SurveyRecord mapToSurveyRecord(CreateSurveyRecordDto dto,
                                   Survey survey,
                                   Account account,
                                   List<AnswerRecord> answerRecords);

    @Mapping(target = "id", source = "surveyRecord.id")
    @Mapping(target = "answerRecords", source = "surveyRecord.answerRecords")
    @Mapping(target = "survey", source = "surveyRecord.survey")
    @Mapping(target = "studentDto", source = "student", qualifiedByName = "mapStudentWithoutEvaluations")
    SurveyRecordResponse mapToSurveyRecordResponse(SurveyRecord surveyRecord,
                                                   Student student);
}