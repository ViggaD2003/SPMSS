package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.SubmitAnswerRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.mapper.AnswerRecordMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Answer;
import com.fpt.gsu25se47.schoolpsychology.model.AnswerRecord;
import com.fpt.gsu25se47.schoolpsychology.model.Question;
import com.fpt.gsu25se47.schoolpsychology.repository.AnswerRecordRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.AnswerRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.QuestRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AnswerRecordService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AnswerRecordServiceImpl implements AnswerRecordService {

    private final AnswerRecordRepository answerRecordRepository;

    private final QuestRepository questRepository;

    private final AnswerRepository answerRepository;

    private final AnswerRecordMapper answerRecordMapper;

    @Override
    @Transactional
    public AnswerRecord createAnswerRecord(SubmitAnswerRecordRequest request) {

        Question question = questRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Question not found with ID: " + request.getQuestionId()
                ));

        Answer answer = null;

        if (request.isSkipped()) {
            if (question.isRequired()) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Required question cannot be skipped. Question ID: " + question.getId()
                );
            }

            if (request.getAnswerId() != null) {
                answer = getAnswer(request, question);
            }

        } else {

            if (request.getAnswerId() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Answer ID must be provided if the question is not skipped."
                );
            }

            answer = getAnswer(request, question);
        }

        AnswerRecord answerRecord = answerRecordMapper.mapToAnswerRecord(request, answer, question);

        return answerRecordRepository.save(answerRecord);
    }

    private Answer getAnswer(SubmitAnswerRecordRequest request, Question question) {

        Answer answer = answerRepository.findById(request.getAnswerId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Answer not found with ID: " + request.getAnswerId()
                ));

        if (!answer.getQuestion().getId().equals(question.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Answer does not belong to this question"
            );
        }
        return answer;
    }
}
