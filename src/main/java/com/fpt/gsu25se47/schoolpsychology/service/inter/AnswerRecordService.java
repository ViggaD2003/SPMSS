package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.SubmitAnswerRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.model.AnswerRecord;

public interface AnswerRecordService {

    AnswerRecord createAnswerRecord(SubmitAnswerRecordRequest request);
}
