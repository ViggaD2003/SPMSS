package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.model.SurveyRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SurveyRecordMapper {
    private final SurveyMapper surveyMapper;
    private final LevelMapper levelMapper;
    private final AnswerRecordMapper answerRecordMapper;

    public SurveyRecordGetAllResponse mapToSurveyRecordGetAllResponse(SurveyRecord surveyRecord) {
        return SurveyRecordGetAllResponse.builder()
                .id(surveyRecord.getId())
                .totalScore(surveyRecord.getTotalScore())
                .isSkipped(surveyRecord.getIsSkipped())
                .completedAt(surveyRecord.getCompletedAt())
                .level(surveyRecord.getLevel() == null ? null : levelMapper.mapToLevelResponse(surveyRecord.getLevel()))
                .survey(surveyMapper.mapToSurveyGetAllResponse(surveyRecord.getSurvey()))
                .build();
    }

    public SurveyRecordDetailResponse mapToSurveyRecordResponse(SurveyRecord surveyRecord) {
        return SurveyRecordDetailResponse.builder()
                .id(surveyRecord.getId())
                .totalScore(surveyRecord.getTotalScore())
                .isSkipped(surveyRecord.getIsSkipped())
                .completedAt(surveyRecord.getCompletedAt())
                .level(surveyRecord.getLevel() == null ? null : levelMapper.mapToLevelResponse(surveyRecord.getLevel()))
                .survey(surveyMapper.mapToSurveyGetAllResponse(surveyRecord.getSurvey()))
                .answerRecords(surveyRecord.getAnswerRecords() == null ?
                        null : surveyRecord.getAnswerRecords().stream().map(answerRecordMapper::mapToAnswerRecordResponse).toList())
                .build();
    }
}
