package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.SubmitAnswerRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Answer;
import com.fpt.gsu25se47.schoolpsychology.model.AnswerRecord;
import com.fpt.gsu25se47.schoolpsychology.model.Question;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = QuestionMapper.class)
public interface AnswerRecordMapper {

    @Mapping(target = "questionResponse", source = "answerRecord.answer.question")
    @Mapping(target = "answerResponse", source = "answerRecord.answer")
    AnswerRecordResponse mapToAnswerRecordResponse(AnswerRecord answerRecord);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(builder = @Builder(disableBuilder = true))
    AnswerRecord mapToAnswerRecord(SubmitAnswerRecordRequest request,
                                   @Context Answer answer,
                                   @Context Question question);

    @AfterMapping
    default void setAnswerAndQuestion(@MappingTarget AnswerRecord answerRecord,
                                      @Context Answer answer,
                                      @Context Question question) {
        answerRecord.setAnswer(answer);
        answerRecord.setQuestion(question);
    }
}
