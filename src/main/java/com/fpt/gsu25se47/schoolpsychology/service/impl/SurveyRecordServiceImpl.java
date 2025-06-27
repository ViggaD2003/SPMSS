package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAnswerRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SubmitAnswerRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.AnswerRecordMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.SurveyRecordMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.AnswerRecord;
import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import com.fpt.gsu25se47.schoolpsychology.model.SurveyRecord;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRecordRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SurveyRecordServiceImpl implements SurveyRecordService {

    private final SurveyRecordRepository surveyRecordRepository;
    private final SurveyRepository surveyRepository;
    private final AccountRepository accountRepository;
    private final SurveyRecordMapper surveyRecordMapper;
    private final AnswerRecordMapper answerRecordMapper;

    @Override
    @Transactional
    public SurveyRecordResponse createSurveyRecord(CreateSurveyRecordDto dto) {
        try {

            validateAnswerIds(dto.getAnswerRecordRequests());

            Survey survey = surveyRepository.findById(dto.getSurveyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Survey not found with ID: " + dto.getSurveyId()));

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!(principal instanceof UserDetails)) {
                throw new BadCredentialsException("Invalid authentication principal");
            }

            String email = ((UserDetails) principal).getUsername();

            Account account = accountRepository.findByEmail(email)
                    .orElseThrow(() -> new BadCredentialsException("Account not found for email: " + email));

            List<AnswerRecord> answerRecords = dto.getAnswerRecordRequests()
                    .stream()
                    .map(t -> {
                        var createAnswerRecordRequest = CreateAnswerRecordRequest.builder()
                                .submitAnswerRecordRequests(t)
                                .build();
                        return answerRecordMapper.mapToAnswerRecord(createAnswerRecordRequest);
                    })
                    .toList();

            SurveyRecord surveyRecord = SurveyRecord.builder()
                    .survey(survey)
                    .answerRecords(answerRecords)
                    .noteSuggest(dto.getNoteSuggest())
                    .status(dto.getSurveyStatus())
                    .account(account)
                    .completedAt(LocalDate.now())
                    .totalScore(dto.getTotalScore())
                    .build();

            answerRecords.forEach(ar -> ar.setSurveyRecord(surveyRecord));

            SurveyRecord surveyRecordCreated = surveyRecordRepository.save(surveyRecord);

            return surveyRecordMapper.mapToSurveyRecordResponse(surveyRecordCreated);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SurveyRecordResponse> getAllSurveyRecordById(int accountId) {
        return surveyRecordRepository.findAllByAccountId(accountId)
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

    private void validateAnswerIds(List<SubmitAnswerRecordRequest> submitAnswerRecordRequests) {
        Set<Integer> seenAnswerIds = new HashSet<>();

        for (SubmitAnswerRecordRequest submit : submitAnswerRecordRequests) {
            if (!seenAnswerIds.add(submit.getAnswerId())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Duplicate answerId found: " + submit.getAnswerId()
                );
            }
        }
    }
}
