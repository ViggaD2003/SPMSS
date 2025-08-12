package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewCaseDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateCaseRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.CaseGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.CaseGetDetailResponse;

import java.util.List;
import java.util.Optional;

public interface CaseService {

    Optional<?> createCase(AddNewCaseDto dto);

    Optional<?> assignCounselor(Integer counselorId, List<Integer> caseId);

    Optional<?> getAllCases(List<String> statusCase, Integer categoryId, Integer surveyId, Integer accountId);

    Optional<?> getDetailById(Integer caseId);

    Optional<?> addSurveyCaseLink(List<Integer> caseIds, Integer surveyId);

    Optional<?> removeSurveyCaseLink(List<Integer> caseIds);

    Optional<?> removeSurveyByCaseId(List<Integer> caseIds, Integer surveyId);

    Optional<CaseGetAllResponse> updateCase(Integer caseId, UpdateCaseRequest request);
}
