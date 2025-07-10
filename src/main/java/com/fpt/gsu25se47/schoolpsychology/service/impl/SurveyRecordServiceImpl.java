package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateMentalEvaluationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.SurveyRecordMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.SurveyRecord;
import com.fpt.gsu25se47.schoolpsychology.model.enums.EvaluationType;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRecordRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.MentalEvaluationService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyRecordService;
import com.fpt.gsu25se47.schoolpsychology.utils.DuplicateValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SurveyRecordServiceImpl implements SurveyRecordService {

    private final SurveyRecordRepository surveyRecordRepository;
    private final AccountRepository accountRepository;
    private final SurveyRecordMapper surveyRecordMapper;
    private final DuplicateValidationUtils duplicateValidationUtils;
    private final MentalEvaluationService mentalEvaluationService;
    private final AccountService accountService;

    @Override
    @Transactional
    public SurveyRecordResponse createSurveyRecord(CreateSurveyRecordDto dto) {
        try {
            if (dto.getStatus() == SurveyRecordStatus.SKIPPED) {
                dto.setTotalScore(null);
                dto.setAnswerRecordRequests(Collections.emptyList());
                dto.setCategoryId(null);
                SurveyRecord skippedRecord = surveyRecordMapper.mapToSurveyRecord(dto);
                SurveyRecord saved = surveyRecordRepository.save(skippedRecord);
                return surveyRecordMapper.mapToSurveyRecordResponse(saved);
            }

            duplicateValidationUtils.validateAnswerIds(dto.getAnswerRecordRequests());

            SurveyRecord surveyRecord = surveyRecordMapper.mapToSurveyRecord(dto);

            surveyRecord.getAnswerRecords().forEach(ar -> ar.setSurveyRecord(surveyRecord));

            SurveyRecord surveyRecordCreated = surveyRecordRepository.save(surveyRecord);

            CreateMentalEvaluationRequest request = CreateMentalEvaluationRequest.builder()
                    .evaluationRecordId(surveyRecord.getId())
                    .date(surveyRecord.getCompletedAt())
                    .evaluationType(EvaluationType.SURVEY)
                    .totalScore(surveyRecord.getTotalScore())
                    .studentId(accountService.getCurrentAccount().getId())
                    .categoryId(dto.getCategoryId())
                    .build();

            mentalEvaluationService.createMentalEvaluation(request);

            return surveyRecordMapper.mapToSurveyRecordResponse(surveyRecordCreated);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<SurveyRecordResponse> getAllSurveyRecordById(int accountId, Pageable pageable) {

        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found for ID: " + accountId));

        return surveyRecordRepository.findAllByAccountId(account.getId(), pageable)
                .map(surveyRecordMapper::mapToSurveyRecordResponse);
    }

    @Override
    public SurveyRecordResponse getSurveyRecordById(int surveyRecordId) {
        SurveyRecord surveyRecord = surveyRecordRepository.findById(surveyRecordId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Survey Record not found with Id: " + surveyRecordId));

        return surveyRecordMapper.mapToSurveyRecordResponse(surveyRecord);
    }

    @Override
    public Page<SurveyRecordResponse> getAllSurveyRecords(Pageable pageable) {
        return surveyRecordRepository.findAll(pageable)
                .map(surveyRecordMapper::mapToSurveyRecordResponse);
    }
}
