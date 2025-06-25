package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.model.SurveyRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SurveyRecordMapper {

    private final AnswerRecordMapper answerRecordResponseMapper;

    public SurveyRecordResponse mapToSurveyRecordResponse(SurveyRecord surveyRecord) {

        List<AnswerRecordResponse> answerRecordResponses = surveyRecord.getAnswerRecords()
                .stream()
                .map(answerRecordResponseMapper::mapToAnswerRecordResponse)
                .toList();

        return SurveyRecordResponse.builder()
                .id(surveyRecord.getId())
                .surveyId(surveyRecord.getSurvey().getId())
                .answerRecords(answerRecordResponses)
                .totalScore(surveyRecord.getTotalScore())
                .noteSuggest(surveyRecord.getNoteSuggest())
                .completedAt(surveyRecord.getCompletedAt())
                .accountFullName(surveyRecord.getAccount().getUsername())
                .mentalEvaluationId(surveyRecord.getMentalEvaluation().getId())
                .accountId(surveyRecord.getAccount().getId())
                .status(surveyRecord.getStatus().name())
                .build();
    }
}
