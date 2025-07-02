package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateMentalEvaluationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.MentalEvaluationResponse;
import com.fpt.gsu25se47.schoolpsychology.model.enums.EvaluationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface MentalEvaluationService {
    MentalEvaluationResponse createMentalEvaluation(CreateMentalEvaluationRequest request);
    MentalEvaluationResponse getMentalEvaluationById(int mentalEvaluationId);
    Page<MentalEvaluationResponse> getMentalEvaluationsByAccountId(int studentId, LocalDate from, LocalDate to, EvaluationType evaluationType, Pageable pageable);
    Page<MentalEvaluationResponse> getAllMentalEvaluations(LocalDate from, LocalDate to, EvaluationType evaluationType, Pageable pageable);
}