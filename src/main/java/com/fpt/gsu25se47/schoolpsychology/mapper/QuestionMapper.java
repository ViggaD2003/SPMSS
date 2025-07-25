package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewQuestionDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.QuestionDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.QuestionResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuestionMapper {

    private final AnswerMapper answerMapper;

    public QuestionResponse mapToQuestionResponse(Question question) {
        return QuestionResponse.builder()
                .questionId(question.getId())
                .updatedAt(question.getUpdatedDate())
                .createdAt(question.getCreatedDate())
                .text(question.getText())
                .description(question.getDescription())
                .isActive(question.getIsActive())
                .isRequired(question.getIsRequired())
                .questionType(question.getQuestionType().name())
                .answers(
                        question.getAnswers()
                                .stream()
                                .map(answerMapper::mapToAnswerResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public Question mapToQuestion(AddNewQuestionDto dto) {
        return Question.builder()
                .description(dto.getDescription())
                .text(dto.getText())
                .isActive(true)
                .isRequired(dto.getIsRequired())
                .questionType(dto.getQuestionType())
                .answers(
                        dto.getAnswers()
                                .stream()
                                .map(answerMapper::mapToAnswer)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public QuestionDto mapToQuestionDto(Question question) {
        return QuestionDto.builder()
                .questionId(question.getId())
                .updatedAt(question.getUpdatedDate())
                .createdAt(question.getCreatedDate())
                .text(question.getText())
                .description(question.getDescription())
                .isActive(question.getIsActive())
                .isRequired(question.getIsRequired())
                .questionType(question.getQuestionType().name())
                .build();
    }
}
