package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewAnswerDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewQuestionDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewSurveyDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSurveyRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.mapper.QuestionMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.SurveyMapper;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyService;
import com.fpt.gsu25se47.schoolpsychology.utils.CurrentAccountUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;

    private final AccountRepository accountRepository;

    private final CategoryRepository categoryRepository;

    private final SurveyMapper surveyMapper;

    private final QuestionMapper questionMapper;
    @Override
    @Transactional
    public Optional<?> addNewSurvey(AddNewSurveyDto addNewSurveyDto, HttpServletRequest request) {
        try {
            UserDetails userDetails = CurrentAccountUtils.getCurrentUser();
            if (userDetails == null) {
                throw new BadRequestException("Unauthorized");
            }

            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new BadRequestException("Unauthorized"));

            Survey survey = surveyMapper.mapToSurvey(addNewSurveyDto, categoryRepository);

            if (!survey.getQuestions().isEmpty()) {
                for (Question question : survey.getQuestions()) {
                    question.setSurvey(survey);

                    if (question.getAnswers() != null) {
                        for (Answer answer : question.getAnswers()) {
                            answer.setQuestion(question);
                        }
                    }
                }
            }

            if (LocalDate.now().isEqual(addNewSurveyDto.getStartDate())) {
                survey.setStatus(SurveyStatus.PUBLISHED);
            } else {
                survey.setStatus(SurveyStatus.DRAFT);
            }

            survey.setCreateBy(account);
            survey.setIsUsed(Boolean.FALSE);
            surveyRepository.save(survey);

            return Optional.of("Create survey successfull");

        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Could not create survey. Please check your data.");
        }
    }

    @Override
    public Optional<?> getAllSurveys() {
        try {
            List<Survey> surveys = surveyRepository.findAll();
            List<SurveyGetAllResponse> surveyResponses = surveys.stream().map(surveyMapper::mapToSurveyGetAllResponse).toList();

            surveyResponses.forEach(response -> {
                surveys.forEach(survey -> response.setCreatedBy(survey.getCreateBy().getId()));
            });

            return Optional.of(surveyResponses);
        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public Optional<?> getSurveyById(Integer id) {
        try {
            Survey survey = surveyRepository.findById(id).orElse(null);

            if (survey == null) {
                throw new RuntimeException("Survey not found");
            }

            SurveyDetailResponse response = surveyMapper.mapToSurveyDetailResponse(survey);
            return Optional.of(response);
        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    @Transactional
    public Optional<?> updateSurveyById(Integer id, UpdateSurveyRequest dto) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found with id: " + id));

        // Validate input dates and recurring rules
        validateUpdateSurveyRequest(dto);

        boolean isUsed = Boolean.TRUE.equals(survey.getIsUsed());

        switch (survey.getStatus()) {
            case DRAFT -> {
                updateBasicSurveyInfo(survey, dto);
                if (!isUsed) {
                    updateAllSurveyInfo(survey, dto);
                }
            }

            case ARCHIVED -> {
                updateBasicSurveyInfo(survey, dto);
                updateAllSurveyInfo(survey, dto);
                survey.setStatus(SurveyStatus.DRAFT); // revive from archive
            }

            default -> throw new IllegalStateException("Cannot update survey with status: " + survey.getStatus());
        }

        Survey updatedSurvey = surveyRepository.save(survey);
        return Optional.of(surveyMapper.mapToSurveyDetailResponse(updatedSurvey));
    }



    @Override
    public Optional<?> getAllSurveyByCounselorId() {
        try {
            UserDetails userDetails = CurrentAccountUtils.getCurrentUser();
            if (userDetails == null) {
                throw new BadRequestException("Unauthorized");
            }

            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Account not found"));

            if (account.getRole().name() != "COUNSELOR") {
                throw new BadRequestException("Account is not counselor");
            }

            List<Survey> surveys = surveyRepository.findByAccountId(account.getId());

            List<SurveyGetAllResponse> surveyResponses = surveys.stream().map(surveyMapper::mapToSurveyGetAllResponse).toList();
            return Optional.of(surveyResponses);
        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public Optional<?> getAllSurveyWithPublished() {
        try {
            UserDetails userDetails = CurrentAccountUtils.getCurrentUser();
            if (userDetails == null) {
                throw new BadRequestException("Unauthorized");
            }

            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Account not found"));

            List<Survey> surveys = surveyRepository.findUnansweredExpiredSurveysByAccountId(account.getId());

            List<SurveyGetAllResponse> surveyResponses = surveys.stream().map(surveyMapper::mapToSurveyGetAllResponse).toList();
            return Optional.of(surveyResponses);
        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public Optional<?> getAllSurveyStudentInCase() {
        try {
            UserDetails userDetails = CurrentAccountUtils.getCurrentUser();
            if (userDetails == null) {
                throw new BadRequestException("Unauthorized");
            }

            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Account not found"));
            List<Survey> surveys = surveyRepository.findAllSurveyStudentInCase(account.getId());
            List<SurveyGetAllResponse> surveyGetAllResponses = surveys.stream().map(surveyMapper::mapToSurveyGetAllResponse).toList();

            return Optional.of(surveyGetAllResponses);
        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    private void validateUpdateSurveyRequest(UpdateSurveyRequest dto) {
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new IllegalArgumentException("Start and end date cannot be null");
        }

        if (dto.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải là hôm nay hoặc trong tương lai");
        }

        if (dto.getEndDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày kết thúc phải trong tương lai");
        }

        if (!dto.getEndDate().isAfter(dto.getStartDate())) {
            throw new RuntimeException("End date must be after start date");
        }

        if (dto.getIsRequired() && dto.getRecurringCycle() == null) {
            throw new RuntimeException("Recurring cycle is required when survey is required");
        }
    }


    private void updateBasicSurveyInfo(Survey survey, UpdateSurveyRequest dto) {
        survey.setStartDate(dto.getStartDate());
        survey.setEndDate(dto.getEndDate());
        survey.setIsRequired(dto.getIsRequired());
        survey.setIsRecurring(dto.getIsRecurring());
        survey.setRecurringCycle(dto.getRecurringCycle());
    }

    private void updateAllSurveyInfo(Survey survey, UpdateSurveyRequest dto) {
        survey.setTitle(dto.getTitle());
        survey.setDescription(dto.getDescription());
        survey.setSurveyType(dto.getSurveyType());
        survey.setTargetScope(dto.getTargetScope());
        survey.setTargetGradeLevel(dto.getTargetGrade());

        updateBasicSurveyInfo(survey, dto);

        survey.getQuestions().clear();
        List<Question> newQuestions = dto.getNewQuestions().stream()
                .map(questionMapper::mapToQuestion)
                .peek(q -> {
                    q.setSurvey(survey);
                    if (q.getAnswers() != null) {
                        q.getAnswers().forEach(a -> a.setQuestion(q));
                    }
                }).toList();

        survey.getQuestions().addAll(newQuestions);
    }
}