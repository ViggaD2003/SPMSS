package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateMentalEvaluationRequest;
import com.fpt.gsu25se47.schoolpsychology.mapper.MentalEvaluationMapper;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordIdentify;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordType;
import com.fpt.gsu25se47.schoolpsychology.repository.MentalEvaluationRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRecordRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.MentalEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;

@Service
@RequiredArgsConstructor
public class MentalEvaluationServiceImpl implements MentalEvaluationService {

    private final MentalEvaluationMapper mentalEvaluationMapper;
    private final MentalEvaluationRepository mentalEvaluationRepository;
    private final SurveyRecordRepository surveyRecordRepository;

    @Override
    public MentalEvaluation createMentalEvaluationWithContext(Appointment appointment, SurveyRecord surveyRecord, ProgramParticipants participants) {

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
        } else if (surveyRecord != null) {
            CreateMentalEvaluationRequest mentalEvaluationRequest = mentalEvaluationMapper.fromSurveyRecord(surveyRecord);
            mentalEvaluationRequest.setWeightedScore(getWeightedScoreForSurveyRecord(surveyRecord));

            MentalEvaluation mappedMentalEvaluation = mentalEvaluationMapper.toMentalEvaluation(mentalEvaluationRequest);
            Account student = surveyRecord.getStudent();
            mappedMentalEvaluation.setStudent(student);
            mappedMentalEvaluation.setSurveyRecord(surveyRecord);
            return mentalEvaluationRepository.save(mappedMentalEvaluation);
        } else if (participants != null) {
            CreateMentalEvaluationRequest mentalEvaluationRequest = mentalEvaluationMapper.fromProgramParticipant(participants);

            List<SurveyRecord> surveyRecords = surveyRecordRepository.findTwoSurveyRecordsByParticipant(participants.getId());

            Float entryWeightScore = getWeightedScoreForSurveyRecord(Objects.requireNonNull(surveyRecords.stream()
                    .filter(sr -> sr.getSurveyRecordIdentify() == SurveyRecordIdentify.ENTRY)
                    .findFirst().orElse(null)));

            Float exitWeightScore = getWeightedScoreForSurveyRecord(Objects.requireNonNull(surveyRecords.stream()
                    .filter(sr -> sr.getSurveyRecordIdentify() == SurveyRecordIdentify.EXIT)
                    .findFirst().orElse(null)));

            MentalEvaluation mappedMentalEvaluation = mentalEvaluationMapper.toMentalEvaluation(mentalEvaluationRequest);
            Account student = participants.getStudent();
            mappedMentalEvaluation.setStudent(student);
            mappedMentalEvaluation.setProgramParticipants(participants);
            mappedMentalEvaluation.setWeightedScore((entryWeightScore + exitWeightScore) / 2);
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

    private Float getWeightedScoreForSurveyRecord(SurveyRecord surveyRecord) {
        Category category = surveyRecord.getSurvey().getCategory();
        Float weightedScore = surveyRecord.getTotalScore() * category.getSeverityWeight();
        return weightedScore / (category.getMaxScore() * category.getQuestionLength()) * 4;
    }
}