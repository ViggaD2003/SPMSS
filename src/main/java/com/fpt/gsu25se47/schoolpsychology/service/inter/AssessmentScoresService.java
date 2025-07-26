package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAssessmentScoreRequest;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.AssessmentScores;

public interface AssessmentScoresService {

    AssessmentScores createAssessmentScoresWithContext(CreateAssessmentScoreRequest request, Appointment appointment);
}
