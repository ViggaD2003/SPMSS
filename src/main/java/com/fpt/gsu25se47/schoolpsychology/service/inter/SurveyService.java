package com.fpt.gsu25se47.schoolpsychology.service.inter;


import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewSurveyDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSurveyRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyGetAllResponse;
import java.util.List;
import java.util.Optional;

public interface SurveyService {

    Integer addNewSurvey(AddNewSurveyDto addNewSurveyDto);

    List<SurveyGetAllResponse> getAllSurveys();

    Optional<?> getSurveyById(Integer id);

    Optional<?> updateSurveyById(Integer id, UpdateSurveyRequest updateSurveyRequest);

    List<SurveyGetAllResponse> getAllSurveyByCounselorId();

    List<SurveyGetAllResponse> getAllSurveyWithPublished(Integer studentId);

    List<SurveyGetAllResponse> getAllSurveyStudentInCase(Integer caseId);
}
