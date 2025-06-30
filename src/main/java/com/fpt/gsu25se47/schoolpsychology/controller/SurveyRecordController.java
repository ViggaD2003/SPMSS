package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.common.ApiResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/survey-records")
public class SurveyRecordController {

    private final SurveyRecordService surveyRecordService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<SurveyRecordResponse>> createSurveyRecord(@RequestBody CreateSurveyRecordDto createSurveyRecordDto) {

        SurveyRecordResponse response = surveyRecordService.createSurveyRecord(createSurveyRecordDto);
        return ResponseEntity.ok(ApiResponse.<SurveyRecordResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Survey Record created successfully")
                .data(response)
                .build());
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<ApiResponse<List<SurveyRecordResponse>>> getAllSurveyRecordsByAccountId(@PathVariable int accountId) {

        List<SurveyRecordResponse> surveyRecordResponses = surveyRecordService.getAllSurveyRecordById(accountId);
        return ResponseEntity.ok(ApiResponse.<List<SurveyRecordResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Retrieve survey records by account id: " + accountId + " successfully")
                .data(surveyRecordResponses)
                .build());
    }

    @GetMapping("/{surveyRecordId}")
    public ResponseEntity<ApiResponse<SurveyRecordResponse>> getSurveyRecordById(@PathVariable int surveyRecordId) {

        SurveyRecordResponse surveyRecordResponse = surveyRecordService.getSurveyRecordById(surveyRecordId);
        return ResponseEntity.ok(ApiResponse.<SurveyRecordResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Retrieve survey record by Id: " + surveyRecordId + " successfully")
                .data(surveyRecordResponse)
                .build());
    }
}