package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.SurveyRecordMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.SurveyRecord;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRecordRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyRecordService;
import com.fpt.gsu25se47.schoolpsychology.utils.AnswerRecordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyRecordServiceImpl implements SurveyRecordService {

    private final SurveyRecordRepository surveyRecordRepository;
    private final AccountRepository accountRepository;
    private final SurveyRecordMapper surveyRecordMapper;
    private final AnswerRecordUtil answerRecordUtil;

    @Override
    @Transactional
    public SurveyRecordResponse createSurveyRecord(CreateSurveyRecordDto dto) {
        try {
            if (dto.getStatus() == SurveyRecordStatus.SKIPPED) {
                dto.setTotalScore(null);
                SurveyRecord skippedRecord = surveyRecordMapper.mapToSurveyRecord(dto);
                SurveyRecord saved = surveyRecordRepository.save(skippedRecord);
                return surveyRecordMapper.mapToSurveyRecordResponse(saved);
            }

            answerRecordUtil.validateAnswerIds(dto.getAnswerRecordRequests());

            SurveyRecord surveyRecord = surveyRecordMapper.mapToSurveyRecord(dto);

            surveyRecord.getAnswerRecords().forEach(ar -> ar.setSurveyRecord(surveyRecord));

            SurveyRecord surveyRecordCreated = surveyRecordRepository.save(surveyRecord);

            return surveyRecordMapper.mapToSurveyRecordResponse(surveyRecordCreated);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SurveyRecordResponse> getAllSurveyRecordById(int accountId) {

        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found for ID: " + accountId));

        return surveyRecordRepository.findAllByAccountId(account.getId())
                .stream()
                .map(surveyRecordMapper::mapToSurveyRecordResponse)
                .toList();
    }

    @Override
    public SurveyRecordResponse getSurveyRecordById(int surveyRecordId) {
        SurveyRecord surveyRecord = surveyRecordRepository.findById(surveyRecordId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Survey Record not found with Id: " + surveyRecordId));

        return surveyRecordMapper.mapToSurveyRecordResponse(surveyRecord);
    }

    @Override
    public List<SurveyRecordResponse> getAllSurveyRecords() {
        return surveyRecordRepository.findAll()
                .stream()
                .map(surveyRecordMapper::mapToSurveyRecordResponse)
                .toList();
    }
}
