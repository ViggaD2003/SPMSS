package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateMentalEvaluationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.MentalEvaluationResponse;
import com.fpt.gsu25se47.schoolpsychology.model.enums.EvaluationType;

import java.time.LocalDate;
import java.util.List;

public interface MentalEvaluationService {
    MentalEvaluationResponse createMentalEvaluation(CreateMentalEvaluationRequest request);
    MentalEvaluationResponse getMentalEvaluationById(int mentalEvaluationId);
    List<MentalEvaluationResponse> getMentalEvaluationsByAccountId(int studentId, LocalDate from, LocalDate to, EvaluationType evaluationType, String field, String direction);
    List<MentalEvaluationResponse> getAllMentalEvaluations(LocalDate from, LocalDate to, EvaluationType evaluationType, String field, String direction);
}