package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordResponse;

import java.util.List;

public interface SurveyRecordService {

    SurveyRecordResponse createSurveyRecord(CreateSurveyRecordDto createSurveyRecordDto);
    List<SurveyRecordResponse> getAllSurveyRecordById(int accountId);
    SurveyRecordResponse getSurveyRecordById(int surveyRecordId);
    List<SurveyRecordResponse> getAllSurveyRecords();
}
