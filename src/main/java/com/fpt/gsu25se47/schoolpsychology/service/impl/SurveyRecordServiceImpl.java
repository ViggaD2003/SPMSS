package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateMentalEvaluationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.EvaluationType;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AnswerRecordService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.MentalEvaluationService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyRecordService;
import com.fpt.gsu25se47.schoolpsychology.validations.DuplicateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyRecordServiceImpl implements SurveyRecordService {

    private final SurveyRecordRepository surveyRecordRepository;
    private final AccountRepository accountRepository;
    private final SurveyRepository surveyRepository;
    private final StudentRepository studentRepository;
    private final AnswerRepository answerRepository;
    private final AnswerRecordMapper answerRecordMapper;
    private final SurveyRecordMapper surveyRecordMapper;
    private final DuplicateValidator duplicateValidator;
    private final MentalEvaluationService mentalEvaluationService;
    private final AccountService accountService;
    private final AnswerRecordService answerRecordService;

    @Override
    @Transactional
    public SurveyRecordResponse createSurveyRecord(CreateSurveyRecordDto dto) {
        try {
            Survey survey = surveyRepository.findById(dto.getSurveyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Survey not found with ID: " + dto.getSurveyId()));

            Account account = accountService.getCurrentAccount();

            if (dto.getStatus() == SurveyRecordStatus.SKIPPED) {
                dto.setTotalScore(null);
                dto.setCategoryId(null);
                SurveyRecord skippedRecord = surveyRecordMapper.mapToSurveyRecord(dto,
                        survey,
                        account,
                        Collections.emptyList());

                SurveyRecord saved = surveyRecordRepository.save(skippedRecord);

                Student student = getStudent(saved.getAccount().getId());

                return surveyRecordMapper.mapToSurveyRecordResponse(saved, student);

            }

            List<AnswerRecord> answerRecords = dto.getAnswerRecordRequests()
                    .stream()
                    .map(answerRecordService::createAnswerRecord)
                    .toList();

            duplicateValidator.validateAnswerIds(dto.getAnswerRecordRequests());

            SurveyRecord surveyRecord = surveyRecordMapper.mapToSurveyRecord(dto,
                    survey,
                    account,
                    answerRecords);

            surveyRecord.getAnswerRecords().forEach(ar -> ar.setSurveyRecord(surveyRecord));
            surveyRecord.setRound(survey.getRound());
            SurveyRecord surveyRecordCreated = surveyRecordRepository.save(surveyRecord);

            Student student = getStudent(surveyRecordCreated.getAccount().getId());

            CreateMentalEvaluationRequest request = CreateMentalEvaluationRequest.builder()
                    .evaluationRecordId(surveyRecord.getId())
                    .date(surveyRecord.getCompletedAt())
                    .evaluationType(EvaluationType.SURVEY)
                    .totalScore(surveyRecord.getTotalScore())
                    .studentId(accountService.getCurrentAccount().getId())
                    .categoryId(dto.getCategoryId())
                    .build();

            mentalEvaluationService.createMentalEvaluation(request);

            return surveyRecordMapper.mapToSurveyRecordResponse(surveyRecordCreated, student);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<SurveyRecordResponse> getAllSurveyRecordById(int accountId, Pageable pageable) {

        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found for ID: " + accountId));

        Student student = getStudent(accountId);

        return surveyRecordRepository.findAllByAccountId(account.getId(), pageable)
                .map(t -> surveyRecordMapper.mapToSurveyRecordResponse(t,
                        student));
    }

    @Override
    public SurveyRecordResponse getSurveyRecordById(int surveyRecordId) {
        SurveyRecord surveyRecord = surveyRecordRepository.findById(surveyRecordId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Survey Record not found with Id: " + surveyRecordId));
        Student student = getStudent(surveyRecord.getAccount().getId());

        return surveyRecordMapper.mapToSurveyRecordResponse(surveyRecord, student);
    }

    @Override
    public Page<SurveyRecordResponse> getAllSurveyRecords(Pageable pageable) {
        return surveyRecordRepository.findAll(pageable)
                .map(surveyRecord -> {
                    Student student = getStudent(surveyRecord.getAccount().getId());

                    return surveyRecordMapper.mapToSurveyRecordResponse(surveyRecord, student);
                });
    }

    private Student getStudent(Integer id) {

        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student not found with Id: " + id));
    }
}
