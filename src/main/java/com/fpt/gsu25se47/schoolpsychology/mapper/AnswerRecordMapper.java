package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.SubmitAnswerRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Answer;
import com.fpt.gsu25se47.schoolpsychology.model.AnswerRecord;
import com.fpt.gsu25se47.schoolpsychology.model.Question;
import com.fpt.gsu25se47.schoolpsychology.repository.AnswerRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AnswerRecordMapper {
    private final QuestionMapper questionMapper;

    private final AnswerMapper answerMapper;


    public AnswerRecord mapToAnswerRecord(SubmitAnswerRecordRequest request, QuestRepository questRepository, AnswerRepository answerRepository) {
        if(request.isSkipped()){
            Question question = questRepository.findById(request.getQuestionId())
                    .orElseThrow(() -> new IllegalArgumentException("Not found question"));
            if(!question.getIsRequired()){
                return AnswerRecord.builder()
                        .question(question)
                        .isSkipped(request.isSkipped())
                        .answer(null)
                        .build();
            } else {
                throw new IllegalArgumentException("Conflict with question require !");
            }
        } else {
            Answer answer = answerRepository.findById(request.getAnswerId())
                    .orElseThrow(() -> new IllegalArgumentException("Not found answer"));
            return AnswerRecord.builder()
                    .question(null)
                    .isSkipped(request.isSkipped())
                    .answer(answer)
                    .build();
        }
    }



    public AnswerRecordResponse mapToAnswerRecordResponse(AnswerRecord answerRecord) {
        if(answerRecord.isSkipped()){
            return AnswerRecordResponse.builder()
                    .id(answerRecord.getId())
                    .questionResponse(questionMapper.mapToQuestionDto(answerRecord.getQuestion()))
                    .answerResponse(null)
                    .skipped(answerRecord.isSkipped())
                    .build();
        } else {
            return AnswerRecordResponse.builder()
                    .id(answerRecord.getId())
                    .questionResponse(null)
                    .answerResponse(this.mapToAnswerRecordDto(answerRecord))
                    .skipped(answerRecord.isSkipped())
                    .build();
        }
    }

    public AnswerRecordDto mapToAnswerRecordDto(AnswerRecord answerRecord) {
        return AnswerRecordDto.builder()
                .answerResponse(answerMapper.mapToAnswerResponse(answerRecord.getAnswer()))
                .questionResponse(questionMapper.mapToQuestionDto(answerRecord.getQuestion()))
                .build();
    }
}
