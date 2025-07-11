package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAnswerRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Answer;
import com.fpt.gsu25se47.schoolpsychology.model.AnswerRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = QuestionMapper.class)
public interface AnswerRecordMapper {

    @Mapping(target = "questionResponse", source = "answerRecord.answer.question")
    @Mapping(target = "answerResponse", source = "answerRecord.answer")
    AnswerRecordResponse mapToAnswerRecordResponse(AnswerRecord answerRecord);

    @Mapping(target = "surveyRecord", ignore = true)
    @Mapping(target = "programRecord", ignore = true)
    @Mapping(target = "isSkipped", source = "request.submitAnswerRecordRequests.skipped")
    @Mapping(target = "appointmentRecord", ignore = true)
    @Mapping(target = "answer", source = "answer")
    @Mapping(target = "id", ignore = true)
    AnswerRecord mapToAnswerRecord(CreateAnswerRecordRequest request,
                                   Answer answer);
}
