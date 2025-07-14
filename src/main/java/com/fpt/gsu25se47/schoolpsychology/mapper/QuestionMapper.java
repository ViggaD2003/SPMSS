package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.QuestionResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "createdAt", source = "question.createdDate")
    @Mapping(target = "updatedAt", source = "question.updatedDate")
    @Mapping(target = "isRequired", source = "question.required")
    @Mapping(target = "isActive", source = "question.active")
    QuestionResponse mapToQuestionResponse(Question question);
}
