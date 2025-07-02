package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import org.springframework.stereotype.Component;

@Component
public class SurveyMapper {

    public SurveyResponse mapToSurveyResponse(Survey survey) {

        return SurveyResponse.builder()
                .surveyId(survey.getId())
                .name(survey.getName())
                .description(survey.getDescription())
                .surveyCode(survey.getSurveyCode())
                .isRequired(survey.getIsRequired())
                .recurringCycle(survey.getRecurringCycle())
                .build();
    }
}
