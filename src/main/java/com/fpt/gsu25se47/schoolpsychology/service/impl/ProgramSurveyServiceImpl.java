package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewAnswerDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewProgramSurvey;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewQuestionDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.CategoryResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ProgramSurveyResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.QuestionResponse;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyType;
import com.fpt.gsu25se47.schoolpsychology.repository.CategoryRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.ProgramSurveyRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SupportProgramRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ProgramSurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgramSurveyServiceImpl implements ProgramSurveyService {

    private final ProgramSurveyRepository programSurveyRepository;

    private final CategoryRepository categoryRepository;

    private final SupportProgramRepository supportProgramRepository;

    @Override
    public Optional<?> addNewPrgSurvey(AddNewProgramSurvey addNewProgramSurvey, Integer programSupportId) {

        ProgramSurvey programSurvey = this.mapToProgramSurvey(addNewProgramSurvey, programSupportId);

        programSurvey.getQuestions().forEach(question -> {
            question.setProgramSurvey(programSurvey);

            if (question.getAnswers().isEmpty()) {
                throw new IllegalArgumentException("Answers cannot be empty");
            }

            question.getAnswers().forEach(answer -> {
                answer.setQuestion(question);
            });
        });

        programSurveyRepository.save(programSurvey);

        return Optional.of("Successfully added program survey");
    }

    @Override
    public Optional<?> getAllPrgSurvey(Integer supportProgramId) {
        List<ProgramSurvey> listSupportProgram = programSurveyRepository.findAllBySupportProgramId(supportProgramId);

        List<ProgramSurveyResponse> listResponse = listSupportProgram.stream()
                .map(this::mapToProgramSurveyResponse).toList();

        if (listResponse.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(listResponse);
    }

    @Override
    public Optional<?> updatePrgSurvey(AddNewProgramSurvey addNewProgramSurvey, Integer programSurveyId) {
        ProgramSurvey programSurvey = programSurveyRepository.findById(programSurveyId)
                .orElseThrow(() -> new IllegalArgumentException("Program survey not found"));
        programSurvey.setSurveyType(SurveyType.valueOf(addNewProgramSurvey.surveyType()));

        programSurvey.getQuestions().clear();

        List<Question> newQuestions = addNewProgramSurvey.questionDtos()
                .stream().map(item -> {
                    Question question = this.mapToQuestion(item);
                    question.setProgramSurvey(programSurvey);

                    if (!question.getAnswers().isEmpty()) {
                        question.getAnswers().forEach(answer -> answer.setQuestion(question));
                    }
                    return question;
                }).toList();

        programSurvey.getQuestions().addAll(newQuestions);

        ProgramSurvey updatedProgramSurvey = programSurveyRepository.save(programSurvey);
        return Optional.of(this.mapToProgramSurveyResponse(updatedProgramSurvey));
    }


    private ProgramSurvey mapToProgramSurvey(AddNewProgramSurvey addNewProgramSurvey, Integer programSupportId) {
        if (addNewProgramSurvey.questionDtos().isEmpty()) {
            throw new IllegalArgumentException("Questions cannot be empty");
        }

        SupportProgram supportProgram = supportProgramRepository.findById(programSupportId)
                .orElseThrow(() -> new IllegalArgumentException("Program survey not found"));

        return ProgramSurvey.builder()
                .surveyType(SurveyType.valueOf(addNewProgramSurvey.surveyType()))
                .program(supportProgram)
                .name(addNewProgramSurvey.name())
                .description(addNewProgramSurvey.description())
                .questions(addNewProgramSurvey.questionDtos().stream().map(this::mapToQuestion).toList())
                .build();
    }

    private Question mapToQuestion(AddNewQuestionDto dto) {
        return Question.builder()
                .answers(dto.getAnswers().stream().map(this::mapToAnswer).toList())
                .category(categoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(() -> new IllegalArgumentException("Category not found")))
                .description(dto.getDescription())
                .text(dto.getText())
                .isActive(true)
                .isRequired(dto.isRequired())
                .questionType(dto.getQuestionType())
                .moduleType(dto.getModuleType())
                .build();
    }

    private Answer mapToAnswer(AddNewAnswerDto dto) {
        return Answer.builder()
                .score(dto.getScore())
                .text(dto.getText())
                .build();
    }

    private ProgramSurveyResponse mapToProgramSurveyResponse(ProgramSurvey programSurvey) {
        return ProgramSurveyResponse.builder()
                .id(programSurvey.getId())
                .surveyType(programSurvey.getSurveyType().name())
                .name(programSurvey.getName())
                .description(programSurvey.getDescription())
                .createdAt(programSurvey.getCreatedDate())
                .updatedAt(programSurvey.getUpdatedDate())
                .questions(programSurvey.getQuestions().stream().map(this::mapToQuestionResponse).toList())
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
                .category(mapToCategoryResponse(question.getCategory()))
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

    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .code(category.getCode())
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
