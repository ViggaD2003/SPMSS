package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SurveyRecordService {
    SurveyRecordDetailResponse createSurveyRecord(CreateSurveyRecordDto createSurveyRecordDto);
    Page<SurveyRecordGetAllResponse> getAllSurveyRecordById(SurveyType surveyType, Integer accountId, Pageable pageable);
    SurveyRecordDetailResponse getSurveyRecordById(int surveyRecordId);
}
