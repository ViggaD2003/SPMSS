package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAssessmentScoreRequest;
import com.fpt.gsu25se47.schoolpsychology.mapper.AssessmentScoreMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.AssessmentScores;
import com.fpt.gsu25se47.schoolpsychology.model.Category;
import com.fpt.gsu25se47.schoolpsychology.repository.AssessmentScoresRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.CategoryRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AssessmentScoresService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AssessmentScoresServiceImpl implements AssessmentScoresService {

    private final AssessmentScoresRepository assessmentScoresRepository;
    private final CategoryRepository categoryRepository;
    private final AssessmentScoreMapper assessmentScoreMapper;

    @Override
    public AssessmentScores createAssessmentScoresWithContext(CreateAssessmentScoreRequest request, Appointment appointment) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Category not found for ID: " + request.getCategoryId()));

        AssessmentScores assessmentScores = assessmentScoreMapper.toAssessmentScore(request);
        assessmentScores.setCategory(category);
        assessmentScores.setCompositeScore(getCompositeScore(request));
        assessmentScores.setAppointment(appointment);

        return assessmentScoresRepository.save(assessmentScores);
    }

    private float getCompositeScore(CreateAssessmentScoreRequest request) {

        return (float) (request.getSeverityScore() * 0.4 + request.getFrequencyScore() * 0.2
                + request.getImpairmentScore() * 0.2 + request.getChronicityScore() * 0.2);
    }
}
