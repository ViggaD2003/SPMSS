package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Answer;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {

    public AnswerResponse mapToAnswerResponse(Answer answer) {
        return AnswerResponse.builder()
                .id(answer.getId())
                .text(answer.getText())
                .score(answer.getScore())
                .build();
    }
}
