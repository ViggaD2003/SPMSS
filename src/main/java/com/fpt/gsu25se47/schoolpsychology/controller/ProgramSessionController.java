package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.common.ApiResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateProgramSessionRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ProgramSessionResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ProgramSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/program-sessions")
@RequiredArgsConstructor
public class ProgramSessionController {

    private final ProgramSessionService programSessionService;

    @PostMapping
    ResponseEntity<ApiResponse<ProgramSessionResponse>> createProgramSession(@Valid @RequestBody CreateProgramSessionRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<ProgramSessionResponse>builder()
                        .success(true)
                        .statusCode(HttpStatus.OK.value())
                        .message("Program session created successfully")
                        .data(programSessionService.createProgramSession(request))
                        .build()
        );
    }
}
