package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewAnswerDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewQuestionDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewSurveyDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
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


    @Override
    @Transactional
    public Optional<?> addNewSurvey(AddNewSurveyDto addNewSurveyDto, HttpServletRequest request) {
        try {
            UserDetails userDetails = CurrentAccountUtils.getCurrentUser();
            if (userDetails == null) {
                throw new BadRequestException("Unauthorized");
            }

            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new BadRequestException("Unauthorized"));

            Survey survey = this.mapToSurvey(addNewSurveyDto);

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
            List<SurveyGetAllResponse> surveyResponses = surveys.stream().map(this::mapToSurveyGetAllResponse).toList();

            surveyResponses.forEach(response -> {
               surveys.forEach(survey ->  response.setCreatedBy(survey.getCreateBy().getId()));
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

            SurveyDetailResponse response = this.mapToSurveyDetailResponse(survey);
            return Optional.of(response);
        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

//    @Override
//    @Transactional
//    public Optional<?> updateSurveyById(Integer id, AddNewSurveyDto updateSurveyRequest) {
//        Survey survey = surveyRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Survey not found"));
//
//        switch (survey.getStatus()) {
//            case DRAFT -> {
//                if (survey.getRound() == 1) {
//                    updateBasicSurveyInfo(survey, updateSurveyRequest);
//
//                    // Xóa hết các câu hỏi cũ
//                    survey.getQuestions().clear();
//
//                    // Thêm câu hỏi mới
//                    List<Question> newQuestions = updateSurveyRequest.getQuestions().stream()
//                            .map(dto -> {
//                                Question question = mapToQuestion(dto);
//                                question.setSurvey(survey);
//                                if (question.getAnswers() != null) {
//                                    question.getAnswers().forEach(answer -> answer.setQuestion(question));
//                                }
//                                return question;
//                            }).toList();
//
//                    survey.getQuestions().addAll(newQuestions);
//                } else {
//                    updateBasicSurveyInfo(survey, updateSurveyRequest);
//                    survey.setStartDate(updateSurveyRequest.getStartDate());
//                    survey.setEndDate(updateSurveyRequest.getEndDate());
//                }
//            }
//
//            case ARCHIVED -> {
//                updateBasicSurveyInfo(survey, updateSurveyRequest);
//                survey.setStartDate(updateSurveyRequest.getStartDate());
//                survey.setEndDate(updateSurveyRequest.getEndDate());
//                survey.setRound(survey.getRound() + 1);
//                survey.setStatus(SurveyStatus.DRAFT); // quay về DRAFT để tạo lại bản mới
//            }
//
//            default -> throw new IllegalStateException("Unsupported survey status: " + survey.getStatus());
//        }
//
//        Survey updatedSurvey = surveyRepository.save(survey);
//        return Optional.of(mapToSurveyResponse(updatedSurvey));
//    }


    @Override
    public Optional<?> getAllSurveyByCounselorId() {
        try {
            UserDetails userDetails = CurrentAccountUtils.getCurrentUser();
            if (userDetails == null) {
                throw new BadRequestException("Unauthorized");
            }

            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Account not found"));

            if(account.getRole().name() != "COUNSELOR"){
                throw new BadRequestException("Account is not counselor");
            }

            List<Survey> surveys = surveyRepository.findByAccountId(account.getId());

            List<SurveyGetAllResponse> surveyResponses = surveys.stream().map(this::mapToSurveyGetAllResponse).toList();
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

            List<SurveyGetAllResponse> surveyResponses = surveys.stream().map(this::mapToSurveyGetAllResponse).toList();
            return Optional.of(surveyResponses);
        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }


    private Answer mapToAnswer(AddNewAnswerDto dto) {
        return Answer.builder()
                .score(dto.getScore())
                .text(dto.getText())
                .build();
    }

    private Question mapToQuestion(AddNewQuestionDto dto) {

        return Question.builder()
                .answers(dto.getAnswers().stream().map(this::mapToAnswer).toList())
                .description(dto.getDescription())
                .text(dto.getText())
                .isActive(true)
                .isRequired(dto.getIsRequired())
                .questionType(dto.getQuestionType())
                .build();
    }

    private Survey mapToSurvey(AddNewSurveyDto dto) {
        if (!dto.getEndDate().isAfter(dto.getStartDate())) {
            throw new RuntimeException("End date must be after than start date");
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return Survey.builder()
                .description(dto.getDescription())
                .endDate(dto.getEndDate())
                .isRequired(dto.getIsRequired())
                .isRecurring(dto.getIsRecurring())
                .surveyType(dto.getSurveyType())
                .targetScope(dto.getTargetScope())
                .targetGradeLevel(dto.getTargetGrade())
                .round(1)
                .category(category)
                .title(dto.getTitle())
                .questions(dto.getQuestions().stream().map(this::mapToQuestion).collect(Collectors.toList()))
                .recurringCycle(dto.getRecurringCycle())
                .startDate(dto.getStartDate())
                .build();
    }


    private SurveyGetAllResponse mapToSurveyGetAllResponse(Survey survey) {
        return SurveyGetAllResponse.builder()
                .surveyId(survey.getId())
                .createdAt(survey.getCreatedDate())
                .updatedAt(survey.getUpdatedDate())
                .title(survey.getTitle())
                .status(survey.getStatus().name())
                .isRecurring(survey.getIsRecurring())
                .isRequired(survey.getIsRequired())
                .endDate(survey.getEndDate())
                .startDate(survey.getStartDate())
                .description(survey.getDescription())
                .recurringCycle(survey.getRecurringCycle().name())
                .targetGrade(survey.getTargetGradeLevel().name())
                .targetScope(survey.getTargetScope().name())
                .surveyType(survey.getSurveyType().name())
                .category(this.mapToCategorySurveyResponse(survey.getCategory()))
                .round(survey.getRound())
                .build();
    }

    private SurveyDetailResponse mapToSurveyDetailResponse(Survey survey) {
        return SurveyDetailResponse.builder()
                .surveyId(survey.getId())
                .createdAt(survey.getCreatedDate())
                .updatedAt(survey.getUpdatedDate())
                .title(survey.getTitle())
                .status(survey.getStatus().name())
                .isRecurring(survey.getIsRecurring())
                .isRequired(survey.getIsRequired())
                .endDate(survey.getEndDate())
                .startDate(survey.getStartDate())
                .description(survey.getDescription())
                .recurringCycle(survey.getRecurringCycle().name())
                .targetGrade(survey.getTargetGradeLevel().name())
                .targetScope(survey.getTargetScope().name())
                .surveyType(survey.getSurveyType().name())
                .category(this.mapToCategorySurveyResponse(survey.getCategory()))
                .round(survey.getRound())
                .questions(survey.getQuestions().stream().map(this::mapToQuestionResponse).toList())
                .build();
    }



    private QuestionResponse mapToQuestionResponse(Question question) {
        return QuestionResponse.builder()
                .questionId(question.getId())
                .updatedAt(question.getUpdatedDate())
                .createdAt(question.getCreatedDate())
                .text(question.getText())
                .description(question.getDescription())
                .isActive(question.getIsActive())
                .isRequired(question.getIsRequired())
                .questionType(question.getQuestionType().name())
                .answers(question.getAnswers().stream().map(this::mapToAnswerResponse).toList())
                .build();
    }

    private AnswerResponse mapToAnswerResponse(Answer answer) {
        return AnswerResponse.builder()
                .id(answer.getId())
                .score(answer.getScore())
                .text(answer.getText())
                .build();
    }


    private CategoryResponse mapToCategorySurveyResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .code(category.getCode())
                .name(category.getName())
                .isSum(category.getIsSum())
                .description(category.getDescription())
                .maxScore(category.getMaxScore())
                .minScore(category.getMinScore())
                .questionLength(category.getQuestionLength())
                .isActive(category.getIsActive())
                .severityWeight(category.getSeverityWeight())
                .isLimited(category.getIsLimited())
                .build();
    }

    private void updateBasicSurveyInfo(Survey survey, AddNewSurveyDto dto) {
        survey.setTitle(dto.getTitle());
        survey.setDescription(dto.getDescription());
        survey.setIsRecurring(dto.getIsRecurring());
        survey.setIsRequired(dto.getIsRequired());
        survey.setRecurringCycle(dto.getRecurringCycle());
    }

}