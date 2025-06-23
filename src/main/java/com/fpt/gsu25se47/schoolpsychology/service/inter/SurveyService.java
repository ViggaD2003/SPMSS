package com.fpt.gsu25se47.schoolpsychology.service.inter;


import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewSurveyDto;

import java.util.Optional;

public interface SurveyService {

    Optional<?> addNewSurvey(AddNewSurveyDto addNewSurveyDto);

    Optional<?> getAllSurveys();

    Optional<?> getSurveyById(Integer id);
}
