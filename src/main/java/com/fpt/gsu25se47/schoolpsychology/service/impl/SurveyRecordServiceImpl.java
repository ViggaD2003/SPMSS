package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurveyRecordServiceImpl implements SurveyRecordService {

    private final SurveyRecordRepository surveyRecordRepository;
    private final MentalEvaluationRepository mentalEvaluationRepository;
    private final AnswerRecordRepository answerRecordRepository;
    private final SurveyRepository surveyRepository;
    private final AccountRepository accountRepository;
    private final StudentRepository studentRepository;
    private final CategoryRepository categoryRepository;
    private final AnswerRepository answerRepository;

    @Override
    @Transactional
    public Optional<SurveyRecordResponse> createSurveyRecord(CreateSurveyRecordDto dto) {

        List<AnswerRecord> answerRecords = answerRecordRepository
                .findAllById(dto.getAnswerRecordRequests());

        if (answerRecords.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Answer Record not found");
        }

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

        // Find or create MentalEvaluation
        MentalEvaluation mentalEvaluation = mentalEvaluationRepository.findById(dto.getMentalEvaluationId())
                .orElseGet(() -> {
                    Student student = studentRepository.findById(account.getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Student not found with ID: " + account.getId()));

                    Category category = categoryRepository.findByCode("PSY").get();

                    return mentalEvaluationRepository.save(
                            MentalEvaluation.builder()
                                    .category(category)
                                    .student(student)
                                    .build()
                    );
                });

        SurveyRecord surveyRecord = SurveyRecord.builder()
                .survey(survey)
                .answerRecords(answerRecords)
                .noteSuggest(dto.getNoteSuggest())
                .status(dto.getSurveyStatus())
                .account(account)
                .mentalEvaluation(mentalEvaluation)
                .totalScore(10) // Consider calculating this dynamically
                .completedAt(LocalDate.now())
                .build();

        SurveyRecord surveyRecordCreated = surveyRecordRepository.save(surveyRecord);

        var answerResponses = answerRecords.stream()
                .map(ar -> {
                    Answer answer = answerRepository.findById(ar.getId()).get();

                    return AnswerRecordResponse
                            .builder()
                            .id(ar.getId())
                            .answerResponse(AnswerResponse
                                    .builder()
                                    .id(answer.getId())
                                    .score(answer.getScore())
                                    .text(answer.getText())
                                    .build())
                            .otherAnswer(ar.getOtherAnswer())
                            .skipped(ar.isSkipped())
                            .build();
                })
                .toList();

        SurveyRecordResponse response = SurveyRecordResponse.builder()
                .id(surveyRecordCreated.getId())
                .surveyId(survey.getId())
                .accountId(account.getId())
                .accountFullName(account.getUsername())
                .status(surveyRecord.getStatus().name())
                .noteSuggest(dto.getNoteSuggest())
                .mentalEvaluationId(mentalEvaluation.getId())
                .completedAt(surveyRecord.getCompletedAt())
                .totalScore(surveyRecord.getTotalScore())
                .answerRecords(answerResponses)
                .build();

        return Optional.of(response);
    }
}
