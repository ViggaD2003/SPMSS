package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewAnswerDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewQuestionDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewSurveyDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;

    private final SubTypeRepository subTypeRepository;

    private final AccountRepository accountRepository;

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Optional<?> addNewSurvey(AddNewSurveyDto addNewSurveyDto, HttpServletRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !(auth.getPrincipal() instanceof UserDetails userDetails)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
            if (userDetails == null) {
                throw new BadRequestException("Unauthorized");
            }

            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new BadRequestException("Unauthorized"));

            Survey survey = this.mapToSurvey(addNewSurveyDto);
            List<Question> questions = survey.getQuestions();

            boolean isSameSubType = questions.stream()
                    .map(Question::getSubType)
                    .distinct()
                    .count() <= 1;

            if (!isSameSubType) {
                throw new IllegalStateException("All questions must have the same subType.");
            }

            // (Tùy chọn) Lấy subtype nếu muốn
            SubType commonSubType = questions.isEmpty() ? null : questions.get(0).getSubType();

            assert commonSubType != null;
            survey.setSurveyCode(commonSubType.getCodeName());

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

            survey.setRound(1);
            survey.setAccount(account);
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
            List<SurveyResponse> surveyResponses = surveys.stream().map(this::mapToSurveyResponse).toList();
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

            SurveyResponse response = this.mapToSurveyResponse(survey);
            return Optional.of(response);
        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    @Transactional
    public Optional<?> updateSurveyById(Integer id, AddNewSurveyDto updateSurveyRequest) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        switch (survey.getStatus()) {
            case DRAFT -> {
                if (survey.getRound() == 1) {
                    updateBasicSurveyInfo(survey, updateSurveyRequest);

                    // Xóa hết các câu hỏi cũ
                    survey.getQuestions().clear();

                    // Thêm câu hỏi mới
                    List<Question> newQuestions = updateSurveyRequest.getQuestions().stream()
                            .map(dto -> {
                                Question question = mapToQuestion(dto);
                                question.setSurvey(survey);
                                if (question.getAnswers() != null) {
                                    question.getAnswers().forEach(answer -> answer.setQuestion(question));
                                }
                                return question;
                            }).toList();

                    survey.getQuestions().addAll(newQuestions);

                    // Kiểm tra tất cả câu hỏi có cùng subtype không
                    boolean isSameSubType = survey.getQuestions().stream()
                            .map(Question::getSubType)
                            .distinct()
                            .count() <= 1;

                    if (!isSameSubType) {
                        throw new IllegalStateException("All questions must have the same subType.");
                    }

                    // Cập nhật surveyCode theo subtype
                    SubType commonSubType = survey.getQuestions().isEmpty() ? null : survey.getQuestions().get(0).getSubType();
                    if (commonSubType != null) {
                        survey.setSurveyCode(commonSubType.getCodeName());
                    }
                } else {
                    updateBasicSurveyInfo(survey, updateSurveyRequest);
                    survey.setStartDate(updateSurveyRequest.getStartDate());
                    survey.setEndDate(updateSurveyRequest.getEndDate());
                }
            }

            case ARCHIVED -> {
                updateBasicSurveyInfo(survey, updateSurveyRequest);
                survey.setStartDate(updateSurveyRequest.getStartDate());
                survey.setEndDate(updateSurveyRequest.getEndDate());
                survey.setRound(survey.getRound() + 1);
                survey.setStatus(SurveyStatus.DRAFT); // quay về DRAFT để tạo lại bản mới
            }

            default -> throw new IllegalStateException("Unsupported survey status: " + survey.getStatus());
        }

        Survey updatedSurvey = surveyRepository.save(survey);
        return Optional.of(mapToSurveyResponse(updatedSurvey));
    }


    @Override
    public Optional<?> getAllSurveyByCounselorId() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !(auth.getPrincipal() instanceof UserDetails userDetails)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Account not found"));

            List<Survey> surveys = surveyRepository.findByAccountId(account.getId());

            List<SurveyResponse> surveyResponses = surveys.stream().map(this::mapToSurveyResponse).toList();
            return Optional.of(surveyResponses);
        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public Optional<?> getAllSurveyWithPublished() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !(auth.getPrincipal() instanceof UserDetails userDetails)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Account not found"));

            List<Survey> surveys = surveyRepository.findUnansweredExpiredSurveysByAccountId(account.getId());

            List<SurveyResponse> surveyResponses = surveys.stream().map(this::mapToSurveyResponse).toList();
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
        SubType subType = subTypeRepository.findById(dto.getSubTypeId()).orElseThrow(() ->
                new RuntimeException("SubType not found"));

        return Question.builder()
                .answers(dto.getAnswers().stream().map(this::mapToAnswer).toList())
                .subType(subType)
                .description(dto.getDescription())
                .text(dto.getText())
                .isActive(true)
                .isRequired(dto.isRequired())
                .questionType(dto.getQuestionType())
                .moduleType(dto.getModuleType())
                .build();
    }

    private Survey mapToSurvey(AddNewSurveyDto dto) {
        if (!dto.getEndDate().isAfter(dto.getStartDate())) {
            throw new RuntimeException("End date must be after than start date");
        }

        return Survey.builder()
                .description(dto.getDescription())
                .endDate(dto.getEndDate())
                .isRequired(dto.getIsRequired())
                .isRecurring(dto.getIsRecurring())
                .name(dto.getName())
                .questions(dto.getQuestions().stream().map(this::mapToQuestion).collect(Collectors.toList()))
                .recurringCycle(dto.getRecurringCycle())
                .startDate(dto.getStartDate())
                .build();
    }


    private SurveyResponse mapToSurveyResponse(Survey survey) {
        SubType subType = subTypeRepository.findByCodeName(survey.getSurveyCode());

        return SurveyResponse.builder()
                .surveyId(survey.getId())
                .createdAt(survey.getCreatedDate())
                .updatedAt(survey.getUpdatedDate())
                .name(survey.getName())
                .status(survey.getStatus().name())
                .isRecurring(survey.getIsRecurring())
                .isRequired(survey.getIsRequired())
                .surveyCode(survey.getSurveyCode())
                .endDate(survey.getEndDate())
                .startDate(survey.getStartDate())
                .description(survey.getDescription())
                .recurringCycle(survey.getRecurringCycle())
                .categories(subType == null ? null : this.mapToResponse(subType.getCategory()))
                .questions(survey.getQuestions().stream().map(this::mapToQuestionResponse).toList())
                .build();
    }

    private QuestionResponse mapToQuestionResponse(Question question) {
        return QuestionResponse.builder()
                .questionId(question.getId())
                .updatedAt(question.getUpdatedDate())
                .createdAt(question.getCreatedDate())
                .text(question.getText())
                .moduleType(question.getModuleType().name())
                .description(question.getDescription())
                .isActive(question.isActive())
                .isRequired(question.isRequired())
                .questionType(question.getQuestionType().name())
                .subType(mapToResponse(question.getSubType()))
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
    private SubTypeResponse mapToResponse(SubType subType) {
        return SubTypeResponse.builder()
                .id(subType.getId())
                .codeName(subType.getCodeName())
                .build();
    }

    private CategorySurveyResponse mapToResponse(Category category) {
        return CategorySurveyResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    private void updateBasicSurveyInfo(Survey survey, AddNewSurveyDto dto) {
        survey.setName(dto.getName());
        survey.setDescription(dto.getDescription());
        survey.setIsRecurring(dto.getIsRecurring());
        survey.setIsRequired(dto.getIsRequired());
        survey.setRecurringCycle(dto.getRecurringCycle());
    }

}