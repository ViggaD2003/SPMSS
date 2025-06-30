package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAnswerRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.model.AnswerRecord;
import com.fpt.gsu25se47.schoolpsychology.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class AnswerRecordMapper {

    private final AnswerMapper answerResponseMapper;
    private final QuestionMapper questionMapper;
    private final AnswerRepository answerRepository;

    public AnswerRecordResponse mapToAnswerRecordResponse(AnswerRecord answerRecord) {
        return com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerRecordResponse.builder()
                .id(answerRecord.getId())
                .questionResponse(questionMapper.mapToQuestionResponse(answerRecord.getAnswer().getQuestion()))
                .answerResponse(answerResponseMapper.mapToAnswerResponse(answerRecord.getAnswer()))
                .skipped(answerRecord.isSkipped())
                .build();
    }

    public AnswerRecord mapToAnswerRecord(CreateAnswerRecordRequest request) {
        var answer = answerRepository.findById(
                        request.getSubmitAnswerRecordRequests().getAnswerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Answer not found with Id: " + request.getSubmitAnswerRecordRequests().getAnswerId())
                );

        return AnswerRecord.builder()
                .answer(answer)
                .isSkipped(request.getSubmitAnswerRecordRequests().isSkipped())
                .build();
    }
}
