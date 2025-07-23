//package com.fpt.gsu25se47.schoolpsychology.controller;
//
//import com.fpt.gsu25se47.schoolpsychology.common.ApiResponse;
//import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
//import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordResponse;
//import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyRecordService;
//import com.fpt.gsu25se47.schoolpsychology.utils.PaginationUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("api/v1/survey-records")
//public class SurveyRecordController {
//
//    private final PaginationUtil paginationUtil;
//    private final SurveyRecordService surveyRecordService;
//
//    @PostMapping
//    @PreAuthorize("hasRole('STUDENT')")
//    public ResponseEntity<ApiResponse<SurveyRecordResponse>> createSurveyRecord(@RequestBody CreateSurveyRecordDto createSurveyRecordDto) {
//
//        SurveyRecordResponse response = surveyRecordService.createSurveyRecord(createSurveyRecordDto);
//        return ResponseEntity.ok(ApiResponse.<SurveyRecordResponse>builder()
//                .statusCode(HttpStatus.OK.value())
//                .success(true)
//                .message("Survey Record created successfully")
//                .data(response)
//                .build());
//    }
//
//    @GetMapping("/accounts/{accountId}")
//    public ResponseEntity<ApiResponse<List<SurveyRecordResponse>>> getAllSurveyRecordsByAccountId(
//            @PathVariable int accountId,
//            @RequestParam(required = false) Integer page,
//            @RequestParam(required = false) Integer size,
//            @RequestParam(required = false, defaultValue = "completedAt") String field,
//            @RequestParam(required = false, defaultValue = "desc") String direction
//    ) {
//        PageRequest pageRequest = paginationUtil.getPageRequest(page, size, direction, field);
//
//        Page<SurveyRecordResponse> surveyRecordResponses = surveyRecordService.getAllSurveyRecordById(accountId,
//                pageRequest);
//
//        return ResponseEntity.ok(ApiResponse.<List<SurveyRecordResponse>>builder()
//                .statusCode(HttpStatus.OK.value())
//                .success(true)
//                .message("Retrieve survey records by account id: " + accountId + " successfully")
//                .data(surveyRecordResponses.getContent())
//                .pagination(paginationUtil.getPaginationResponse(pageRequest, surveyRecordResponses))
//                .build());
//    }
//
//    @GetMapping("/{surveyRecordId}")
//    public ResponseEntity<ApiResponse<SurveyRecordResponse>> getSurveyRecordById(@PathVariable int surveyRecordId) {
//
//        SurveyRecordResponse surveyRecordResponse = surveyRecordService.getSurveyRecordById(surveyRecordId);
//        return ResponseEntity.ok(ApiResponse.<SurveyRecordResponse>builder()
//                .statusCode(HttpStatus.OK.value())
//                .success(true)
//                .message("Retrieve survey record by Id: " + surveyRecordId + " successfully")
//                .data(surveyRecordResponse)
//                .build());
//    }
//
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<SurveyRecordResponse>>> getAllSurveyRecords(
//            @RequestParam(required = false) Integer page,
//            @RequestParam(required = false) Integer size,
//            @RequestParam(required = false, defaultValue = "completedAt") String field,
//            @RequestParam(required = false, defaultValue = "desc") String direction
//    ) {
//
//        PageRequest pageRequest = paginationUtil.getPageRequest(page, size, direction, field);
//
//        Page<SurveyRecordResponse> recordResponses = surveyRecordService
//                .getAllSurveyRecords(pageRequest);
//
//        return ResponseEntity.ok(ApiResponse.<List<SurveyRecordResponse>>builder()
//                .statusCode(HttpStatus.OK.value())
//                .success(true)
//                .message("Retrieve survey records successfully")
//                .data(recordResponses.getContent())
//                .pagination(paginationUtil.getPaginationResponse(pageRequest, recordResponses))
//                .build());
//    }
//}