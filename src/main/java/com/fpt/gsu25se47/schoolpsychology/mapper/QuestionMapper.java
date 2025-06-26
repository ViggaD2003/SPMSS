package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.QuestionResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionMapper {

    private final CategoryMapper categoryMapper;

    public QuestionResponse mapToQuestionResponse(Question question) {
        return QuestionResponse.builder()
                .questionId(question.getId())
                .text(question.getText())
                .questionType(question.getQuestionType().name())
                .category(categoryMapper.toResponse(question.getCategory()))
                .description(question.getDescription())
                .moduleType(question.getModuleType().name())
                .build();
    }
}
