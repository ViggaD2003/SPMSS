package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = QuestionMapper.class)
public interface SurveyMapper {

    @Mapping(target = "surveyId", source = "survey.id")
    @Mapping(target = "updatedAt", source = "survey.updatedDate")
    @Mapping(target = "createdAt", source = "survey.createdDate")
    @Mapping(target = "questions", source = "questions")
    SurveyResponse mapToSurveyResponse(Survey survey);
}
