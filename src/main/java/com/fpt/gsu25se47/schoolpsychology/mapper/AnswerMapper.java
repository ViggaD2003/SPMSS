package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewAnswerDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Answer;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {

    public Answer mapToAnswer(AddNewAnswerDto dto) {
        return Answer.builder()
                .score(dto.getScore())
                .text(dto.getText())
                .build();
    }

    public AnswerResponse mapToAnswerResponse(Answer answer) {
        return AnswerResponse.builder()
                .id(answer.getId())
                .score(answer.getScore())
                .text(answer.getText())
                .build();
    }
}
