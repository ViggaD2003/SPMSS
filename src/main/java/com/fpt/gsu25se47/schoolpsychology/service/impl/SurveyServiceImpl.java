package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewAnswerDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewQuestionDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewSurveyDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.CategoryResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.QuestionResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyResponse;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final CategoryRepository categoryRepository;

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public Optional<?> addNewSurvey(AddNewSurveyDto addNewSurveyDto, HttpServletRequest request) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(userDetails == null) {
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

            if(LocalDate.now().isEqual(addNewSurveyDto.getStartDate())){
                survey.setStatus(SurveyStatus.PUBLISHED);
            } else {
                survey.setStatus(SurveyStatus.DRAFT);
            }

            survey.setRound(1);
            survey.setAccount(account);
            surveyRepository.save(survey);

            return Optional.of("Create survey successfull");

        } catch (Exception e){
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
        } catch (Exception e){
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public Optional<?> getSurveyById(Integer id) {
        try {
            Survey survey = surveyRepository.findById(id).orElse(null);

            if(survey == null){
                throw new RuntimeException("Survey not found");
            }

            SurveyResponse response = this.mapToSurveyResponse(survey);
            return Optional.of(response);
        } catch (Exception e){
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    @Transactional
    public Optional<?> updateSurveyById(Integer id, AddNewSurveyDto updateSurveyRequest) {
        try {
            Survey survey = surveyRepository.findById(id).orElseThrow(() ->
                    new RuntimeException("Survey not found"));

            // Cập nhật các field của survey
            survey.setName(updateSurveyRequest.getName());
            survey.setDescription(updateSurveyRequest.getDescription());
            survey.setEndDate(updateSurveyRequest.getEndDate());
            survey.setStartDate(updateSurveyRequest.getStartDate());
            survey.setIsRecurring(updateSurveyRequest.getIsRecurring());
            survey.setIsRequired(updateSurveyRequest.getIsRequired());
            survey.setRecurringCycle(updateSurveyRequest.getRecurringCycle());

            // Xoá và thay thế câu hỏi
            //kĩ thuật update
            //chỉ xoá phần tử trong danh sách trước đó (trước đó là khi tạo mới 1 object đã lưu danh sách đó vào)
            survey.getQuestions().clear();

            List<Question> newQuestions = updateSurveyRequest.getQuestions()
                    .stream()
                    .map(dto -> {
                        Question question = this.mapToQuestion(dto);
                        question.setSurvey(survey);
                        if (question.getAnswers() != null) {
                            question.getAnswers().forEach(answer -> answer.setQuestion(question));
                        }
                        return question;
                    }).toList();

            //sau khi xoá phần tử
            //thì lưu phần tử mới vào danh sách trước đó
            survey.getQuestions().addAll(newQuestions);

            Survey updatedSurvey = surveyRepository.save(survey);
            return Optional.of(this.mapToSurveyResponse(updatedSurvey));

        } catch (Exception e) {
            log.error("Failed to update survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public Optional<?> getAllSurveyByCounselorId() {
        try{
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Account not found"));

            List<Survey> surveys = surveyRepository.findByAccountId(account.getId());

            List<SurveyResponse> surveyResponses = surveys.stream().map(this::mapToSurveyResponse).toList();
            return Optional.of(surveyResponses);
        } catch (Exception e){
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public Optional<?> getAllSurveyWithPublished() {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Account not found"));

            List<Survey> surveys = surveyRepository.findUnansweredExpiredSurveysByAccountId(account.getId());

            List<SurveyResponse> surveyResponses = surveys.stream().map(this::mapToSurveyResponse).toList();
            return Optional.of(surveyResponses);
        } catch (Exception e){
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }


    private Answer mapToAnswer(AddNewAnswerDto dto){
        return Answer.builder()
                .score(dto.getScore())
                .text(dto.getText())
                .build();
    }

    private Question mapToQuestion(AddNewQuestionDto dto){
        return Question.builder()
                .answers(dto.getAnswers().stream().map(this::mapToAnswer).toList())
                .category(categoryRepository.findById(dto.getCategoryId()).orElse(null))
                .description(dto.getDescription())
                .text(dto.getText())
                .isActive(true)
                .isRequired(dto.isRequired())
                .questionType(dto.getQuestionType())
                .moduleType(dto.getModuleType())
                .build();
    }

    private Survey mapToSurvey(AddNewSurveyDto dto) {
        if(dto.getEndDate().isEqual(dto.getStartDate())){
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
                .surveyCode(dto.getSurveyCode())
                .build();
    }


    private SurveyResponse mapToSurveyResponse(Survey survey){
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
                .questions(survey.getQuestions().stream().map(this::mapToQuestionResponse).toList())
                .build();
    }

    private QuestionResponse mapToQuestionResponse(Question question){
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
                .category(mapToCategoryResponse(question.getCategory()))
                .answers(question.getAnswers().stream().map(this::mapToAnswerResponse).toList())
                .build();
    }

    private AnswerResponse mapToAnswerResponse(Answer answer){
        return AnswerResponse.builder()
                .id(answer.getId())
                .score(answer.getScore())
                .text(answer.getText())
                .build();
    }

    private CategoryResponse mapToCategoryResponse(Category category){
        return CategoryResponse.builder()
                .code(category.getCode())
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}