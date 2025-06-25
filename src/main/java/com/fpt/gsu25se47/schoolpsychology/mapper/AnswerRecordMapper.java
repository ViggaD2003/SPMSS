package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.model.AnswerRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerRecordMapper {

    private final AnswerMapper answerResponseMapper;

    public com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerRecordResponse mapToAnswerRecordResponse(AnswerRecord answerRecord) {
        return com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerRecordResponse.builder()
                .id(answerRecord.getId())
                .otherAnswer(answerRecord.getOtherAnswer())
                .answerResponse(answerResponseMapper.mapToAnswerResponse(answerRecord.getAnswer()))
                .skipped(answerRecord.isSkipped())
                .build();
    }
}
