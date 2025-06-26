package com.fpt.gsu25se47.schoolpsychology.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubmitAnswerRecordRequest {
    private Integer answerId;
    private boolean isSkipped;
}
