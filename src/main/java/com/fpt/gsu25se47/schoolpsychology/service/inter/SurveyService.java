package com.fpt.gsu25se47.schoolpsychology.service.inter;


import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewSurveyDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSurveyRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface SurveyService {

    Optional<?> addNewSurvey(AddNewSurveyDto addNewSurveyDto, HttpServletRequest request);

    Optional<?> getAllSurveys();

    Optional<?> getSurveyById(Integer id);

    Optional<?> updateSurveyById(Integer id, UpdateSurveyRequest updateSurveyRequest);

    Optional<?> getAllSurveyByCounselorId();

    Optional<?> getAllSurveyWithPublished();

    Optional<?> getAllSurveyStudentInCase();
}
