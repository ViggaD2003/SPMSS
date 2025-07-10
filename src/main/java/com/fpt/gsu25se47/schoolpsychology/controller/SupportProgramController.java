package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.common.ApiResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SupportProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/support-programs")
@RequiredArgsConstructor
public class SupportProgramController {

    private final SupportProgramService service;

    @PostMapping
    public ResponseEntity<ApiResponse<SupportProgramResponse>> createSupportProgram(@RequestBody CreateSupportProgramRequest request) {

        return ResponseEntity.ok(ApiResponse.<SupportProgramResponse>builder()
                .data(service.createSupportProgram(request))
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Support program created successfully")
                .build());
    }
}
