package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateMentalEvaluationRequest;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.MentalEvaluation;
import com.fpt.gsu25se47.schoolpsychology.model.SurveyRecord;

public interface MentalEvaluationService {
    MentalEvaluation createMentalEvaluationWithContext(CreateMentalEvaluationRequest request, Appointment appointment, SurveyRecord surveyRecord);
}