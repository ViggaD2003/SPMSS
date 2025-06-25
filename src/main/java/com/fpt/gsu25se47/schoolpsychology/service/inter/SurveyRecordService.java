package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordResponse;

import java.util.List;
import java.util.Optional;

public interface SurveyRecordService {

    Optional<SurveyRecordResponse> createSurveyRecord(CreateSurveyRecordDto createSurveyRecordDto);
    List<SurveyRecordResponse> getAllSurveyRecordById(int accountId);
}
