package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewSurveyDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.NotiRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSurveyRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.mapper.QuestionMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.SurveyMapper;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.NotificationService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyService;
import com.fpt.gsu25se47.schoolpsychology.utils.CurrentAccountUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;

    private final AccountRepository accountRepository;

    private final CategoryRepository categoryRepository;

    private final SurveyMapper surveyMapper;

    private final QuestionMapper questionMapper;

    private final NotificationService notificationService;

    @Override
    @Transactional
    public Integer addNewSurvey(AddNewSurveyDto addNewSurveyDto) {
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
            survey.setRound(1);
            Survey saved = surveyRepository.save(survey);

            accountRepository.findAllWithRoleStudent().forEach(student -> {
                NotiResponse payload = notificationService.saveNotification(
                        NotiRequest.builder()
                        .title("Thông báo mới từ trường")
                        .content("Khảo sát " + saved.getTitle() + " mới được thêm")
                        .username(student.getEmail())
                        .notificationType("SURVEY")
                        .relatedEntityId(saved.getId())
                        .build()
                );
                notificationService.sendNotification(student.getEmail(), "/queue/notifications", payload);
            });

            return saved.getId();
        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Could not create survey. Please check your data.");
        }
    }

    @Override
    public List<SurveyGetAllResponse> getAllSurveys() {
        try {
            List<Survey> surveys = surveyRepository.findAll();
            List<SurveyGetAllResponse> surveyResponses = surveys.stream().map(surveyMapper::mapToSurveyGetAllResponse).toList();

            return surveyResponses;
        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public SurveyDetailResponse getSurveyById(Integer id) {
        try {
            Survey survey = surveyRepository.findById(id).orElse(null);

            if (survey == null) {
                throw new RuntimeException("Survey not found");
            }

            SurveyDetailResponse response = surveyMapper.mapToSurveyDetailResponse(survey);
            return response;
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

        switch (survey.getStatus()) {
            case DRAFT -> {
                if (survey.getRound() == 1) {
                    updateAllSurveyInfo(survey, dto, true); // deleteQuestions = true
                } else {
                    // Draft Round > 1: Update fields, round không tăng, questions unlink và thêm mới
                    updateAllSurveyInfo(survey, dto, false); // deleteQuestions = false
                }
            }
            case ARCHIVED -> {
                // Archived: Round +1, update fields, questions unlink và thêm mới
                survey.setRound(survey.getRound() + 1);
                survey.setStatus(SurveyStatus.DRAFT); // revive from archive
                updateAllSurveyInfo(survey, dto, false); // deleteQuestions = false
            }
            default -> throw new IllegalStateException("Cannot update survey with status: " + survey.getStatus());
        }

        Survey updatedSurvey = surveyRepository.save(survey);
        return Optional.of(surveyMapper.mapToSurveyDetailResponse(updatedSurvey));
    }


    @Override
    public List<SurveyGetAllResponse> getAllSurveyByCounselorId() {
        try {
            UserDetails userDetails = CurrentAccountUtils.getCurrentUser();
            if (userDetails == null) {
                throw new BadRequestException("Unauthorized");
            }

            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Account not found"));

            if (!account.getRole().name().equals("COUNSELOR")) {
                throw new BadRequestException("Account is not counselor");
            }

            List<Survey> surveys = surveyRepository.findByAccountId(account.getId());

            List<SurveyGetAllResponse> surveyResponses = surveys.stream().map(surveyMapper::mapToSurveyGetAllResponse).toList();
            return surveyResponses;
        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public List<SurveyGetAllResponse> getAllSurveyWithPublished(Integer studentId) {
        try {
            Account account = accountRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Account not found"));

            List<Survey> surveys = surveyRepository.findUnansweredExpiredSurveysByAccountId(account.getId());

            List<SurveyGetAllResponse> surveyResponses = surveys.stream().map(surveyMapper::mapToSurveyGetAllResponse).toList();
            return surveyResponses;
        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public List<SurveyGetAllResponse> getAllSurveyStudentInCase(Integer caseId) {
        try {
            UserDetails userDetails = CurrentAccountUtils.getCurrentUser();
            if (userDetails == null) {
                throw new BadRequestException("Unauthorized");
            }

            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Account not found"));
            List<Survey> surveys = null;

            if (!account.getRole().name().equals("STUDENT") && caseId != null) {
                surveys = surveyRepository.findAllSurveyByCaseId(caseId);
            } else {
                surveys = surveyRepository.findAllSurveyIsActiveInCase();
            }

            List<SurveyGetAllResponse> surveyGetAllResponses = surveys.stream().map(surveyMapper::mapToSurveyGetAllResponse).toList();

            return surveyGetAllResponses;
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

    private void updateAllSurveyInfo(Survey survey, UpdateSurveyRequest dto, boolean deleteQuestions) {
        // Update basic survey info
        survey.setTitle(dto.getTitle());
        survey.setDescription(dto.getDescription());
        survey.setSurveyType(dto.getSurveyType());
        survey.setTargetScope(dto.getTargetScope());
        survey.setTargetGradeLevel(dto.getTargetGrade());
        updateBasicSurveyInfo(survey, dto);

        // Handle questions based on deleteQuestions flag
        if (deleteQuestions) {
            // Draft Round 1: Delete questions (cascade delete)
            survey.getQuestions().clear();
        } else {
            // Draft Round > 1 or Archived: Unlink questions (không xóa khỏi DB)
            survey.getQuestions().forEach(question -> question.setSurvey(null));
            survey.getQuestions().clear();
        }

        // Add new questions
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