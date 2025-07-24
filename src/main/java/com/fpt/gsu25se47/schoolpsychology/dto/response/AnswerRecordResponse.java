package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerRecordResponse {

    private Integer id;
    private QuestionDto questionResponse;
    private AnswerResponse answerResponse;
    private boolean skipped;
}
