package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateMentalEvaluationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.MentalEvaluationResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.MentalEvaluationMapper;
import com.fpt.gsu25se47.schoolpsychology.model.MentalEvaluation;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import com.fpt.gsu25se47.schoolpsychology.model.enums.EvaluationType;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.MentalEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MentalEvaluationServiceImpl implements MentalEvaluationService {

    private final MentalEvaluationRepository mentalEvaluationRepository;
    private final AppointmentRecordRepository appointmentRecordRepository;
    private final SurveyRecordRepository surveyRecordRepository;
    private final ProgramRecordRepository programRecordRepository;
    private final MentalEvaluationMapper mentalEvaluationMapper;
    private final StudentRepository studentRepository;


    @Override
    public MentalEvaluationResponse createMentalEvaluation(CreateMentalEvaluationRequest request) {
        switch (request.getEvaluationType()) {
            case SURVEY -> surveyRecordRepository.findById(request.getEvaluationRecordId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Survey record not found with ID: " + request.getEvaluationRecordId()
                    ));

            case PROGRAM -> programRecordRepository.findById(request.getEvaluationRecordId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Program record not found with ID: " + request.getEvaluationRecordId()));

            case APPOINTMENT -> appointmentRecordRepository.findById(request.getEvaluationRecordId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Appointment record not found with ID: " + request.getEvaluationRecordId()));

            default -> throw new IllegalStateException("Unexpected value: " + request.getEvaluationType());
        }

        MentalEvaluation mentalEvaluation = mentalEvaluationRepository.save(mentalEvaluationMapper.mapToMentalEvaluation(request));

        return mentalEvaluationMapper.mapToEvaluationResponse(mentalEvaluation);
    }

    @Override
    public MentalEvaluationResponse getMentalEvaluationById(int mentalEvaluationId) {

        MentalEvaluation mentalEvaluation = mentalEvaluationRepository.findById(mentalEvaluationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Mental Evaluation not found"));

        return mentalEvaluationMapper.mapToEvaluationResponse(mentalEvaluation);
    }

    @Override
    public Page<MentalEvaluationResponse> getMentalEvaluationsByAccountId(int studentId, LocalDate from, LocalDate to, EvaluationType evaluationType,
                                                                          Pageable pageable) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student not found"));

        Page<MentalEvaluation> mentalEvaluations = mentalEvaluationRepository
                .findAllByStudentIdAndDateBetweenAndEvaluationType(student.getId(), from,
                        to, evaluationType, pageable);

        return mentalEvaluations.map(mentalEvaluationMapper::mapToEvaluationResponse);
    }

    @Override
    public Page<MentalEvaluationResponse> getAllMentalEvaluations(LocalDate from, LocalDate to, EvaluationType evaluationType, Pageable pageable) {
        Page<MentalEvaluation> mentalEvaluations = mentalEvaluationRepository
                .findAllByDateBetweenAndEvaluationType(from, to, evaluationType, pageable);

        return mentalEvaluations.map(mentalEvaluationMapper::mapToEvaluationResponse);
    }
}
