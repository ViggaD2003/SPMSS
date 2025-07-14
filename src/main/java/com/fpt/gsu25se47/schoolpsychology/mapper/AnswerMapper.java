package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Answer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    AnswerResponse mapToAnswerResponse(Answer answer);
}
