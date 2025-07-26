package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateMentalEvaluationRequest;
import com.fpt.gsu25se47.schoolpsychology.mapper.MentalEvaluationMapper;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.repository.MentalEvaluationRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.MentalEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.OptionalDouble;

@Service
@RequiredArgsConstructor
public class MentalEvaluationServiceImpl implements MentalEvaluationService {

    private final MentalEvaluationMapper mentalEvaluationMapper;
    private final MentalEvaluationRepository mentalEvaluationRepository;

    @Override
    public MentalEvaluation createMentalEvaluationWithContext(Appointment appointment, SurveyRecord surveyRecord) {

        if (appointment != null) {

            CreateMentalEvaluationRequest mentalEvaluationRequest = mentalEvaluationMapper.fromAppointment(appointment);
            mentalEvaluationRequest.setWeightedScore(getWeightedScoreForAppointment(appointment));

            MentalEvaluation mappedMentalEvaluation = mentalEvaluationMapper.toMentalEvaluation(mentalEvaluationRequest);

            Student studentBookedFor = appointment.getBookedFor().getStudent();
            mappedMentalEvaluation.setStudent(studentBookedFor != null
                    ? appointment.getBookedFor().getStudent().getAccount()
                    : appointment.getBookedBy().getStudent().getAccount());

            mappedMentalEvaluation.setAppointment(appointment);
            return mentalEvaluationRepository.save(mappedMentalEvaluation);
        }

        return null;
    }

    private static Float getWeightedScoreForAppointment(Appointment appointment) {
        OptionalDouble averageOpt = appointment.getAssessmentScores().stream()
                .mapToDouble(AssessmentScores::getCompositeScore)
                .average();

        return averageOpt.isPresent()
                ? (float) ((averageOpt.getAsDouble() / 5f) * 4f)
                : 0f;
    }
}