package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordResponse;

import java.util.Optional;

public interface SurveyRecordService {

    Optional<SurveyRecordResponse> createSurveyRecord(CreateSurveyRecordDto createSurveyRecordDto);
}
