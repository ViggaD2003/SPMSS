package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.common.ApiResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/survey-records")
public class SurveyRecordController {

    private final SurveyRecordService surveyRecordService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createSurveyRecord(@RequestBody CreateSurveyRecordDto createSurveyRecordDto) {

        SurveyRecordResponse response = surveyRecordService.createSurveyRecord(createSurveyRecordDto).get();
        return ResponseEntity.ok(ApiResponse.<SurveyRecordResponse>builder()
                .success(true)
                .message("Survey Record created successfully")
                .data(response)
                .build());
    }
}
