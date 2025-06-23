package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewAnswerDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewQuestionDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewSurveyDto;
import com.fpt.gsu25se47.schoolpsychology.model.Answer;
import com.fpt.gsu25se47.schoolpsychology.model.Question;
import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.AnswerRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.CategoryRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.QuestRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;

    private final QuestRepository questRepository;

    private final AnswerRepository answerRepository;

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Optional<?> addNewSurvey(AddNewSurveyDto addNewSurveyDto) {
        try {
            Survey survey = this.mapToSurvey(addNewSurveyDto);
            if (survey.getQuestions() != null) {
                for (Question question : survey.getQuestions()) {
                    question.setSurvey(survey);

                    if (question.getAnswers() != null) {
                        for (Answer answer : question.getAnswers()) {
                            answer.setQuestion(question);
                        }
                    }
                }
            }

            surveyRepository.save(survey);

            return Optional.of("Create survey successfull");

        } catch (Exception e){
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Could not create survey. Please check your data.");
        }
    }

    @Override
    public Optional<?> getAllSurveys() {
        return Optional.empty();
    }

    @Override
    public Optional<?> getSurveyById(Integer id) {
        return Optional.empty();
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
    private Survey mapToSurvey(AddNewSurveyDto dto){
        return Survey.builder()
                .description(dto.getDescription())
                .endDate(dto.getEndDate())
                .isRequired(dto.getIsRequired())
                .isRecurring(dto.getIsRecurring())
                .endDate(dto.getEndDate())
                .name(dto.getName())
                .questions(dto.getQuestions().stream().map(this::mapToQuestion).collect(Collectors.toList()))
                .recurringCycle(dto.getRecurringCycle())
                .startDate(dto.getStartDate())
                .status(SurveyStatus.PENDING)
                .build();
    }
}
