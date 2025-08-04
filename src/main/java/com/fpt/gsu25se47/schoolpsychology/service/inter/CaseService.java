package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewCaseDto;

import java.util.List;
import java.util.Optional;

public interface CaseService {

    Optional<?> createCase(AddNewCaseDto dto);

    Optional<?> assignCounselor(Integer counselorId, Integer caseId);

    Optional<?> getAllCases(List<String> statusCase, Integer categoryId, Integer surveyId);

//    Optional<?> getAllCaseByCategory(Integer categoryId);

    Optional<?> getDetailById(Integer caseId);

    Optional<?> addSurveyCaseLink(List<Integer> caseIds, Integer surveyId);

    Optional<?> removeSurveyCaseLink(List<Integer> caseIds);

    Optional<?> removeSurveyByCaseId(List<Integer> caseIds, Integer surveyId);
}
