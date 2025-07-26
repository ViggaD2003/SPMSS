package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyType;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyRecordService;
import com.fpt.gsu25se47.schoolpsychology.utils.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/survey-records")
public class SurveyRecordController {

    private final PaginationUtil paginationUtil;
    private final SurveyRecordService surveyRecordService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> createSurveyRecord(@RequestBody CreateSurveyRecordDto createSurveyRecordDto) {
        SurveyRecordDetailResponse response = surveyRecordService.createSurveyRecord(createSurveyRecordDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<?> getAllSurveyRecordsByAccountId(
            @PathVariable Integer accountId,
            @RequestParam(name = "surveyType") SurveyType surveyType,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false, defaultValue = "completedAt") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction
    ) {
        PageRequest pageRequest = paginationUtil.getPageRequest(page, size, direction, field);

        Page<SurveyRecordGetAllResponse> surveyRecordResponses = surveyRecordService.getAllSurveyRecordById(surveyType, accountId,
                pageRequest);

        return ResponseEntity.ok(paginationUtil.getPaginationResponse(pageRequest, surveyRecordResponses, surveyRecordResponses.getContent()));
    }

    @GetMapping("/{surveyRecordId}")
    public ResponseEntity<?> getSurveyRecordById(@PathVariable int surveyRecordId) {
        SurveyRecordDetailResponse surveyRecordResponse = surveyRecordService.getSurveyRecordById(surveyRecordId);
        return ResponseEntity.ok(surveyRecordResponse);
    }

//    @GetMapping
//    public ResponseEntity<ApiResponse<List<SurveyRecordDetailResponse>>> getAllSurveyRecords(
//            @RequestParam(required = false) Integer page,
//            @RequestParam(required = false) Integer size,
//            @RequestParam(required = false, defaultValue = "completedAt") String field,
//            @RequestParam(required = false, defaultValue = "desc") String direction
//    ) {
//
//        PageRequest pageRequest = paginationUtil.getPageRequest(page, size, direction, field);
//
//        Page<SurveyRecordDetailResponse> recordResponses = surveyRecordService
//                .getAllSurveyRecords(pageRequest);
//
//        return ResponseEntity.ok(ApiResponse.<List<SurveyRecordDetailResponse>>builder()
//                .statusCode(HttpStatus.OK.value())
//                .success(true)
//                .message("Retrieve survey records successfully")
//                .data(recordResponses.getContent())
//                .pagination(paginationUtil.getPaginationResponse(pageRequest, recordResponses))
//                .build());
//    }
}