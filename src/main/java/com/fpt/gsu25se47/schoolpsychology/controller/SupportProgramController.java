package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SupportProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/support-programs")
@RequiredArgsConstructor
public class SupportProgramController {

    private final SupportProgramService service;

    @Operation(
            summary = "Create a new support program",
            description = "Creates a new support program with details like name, schedule, participants, and category, no sessions attached yet," +
                    "Role: MANAGER, COUNSELOR"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Support program created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SupportProgramResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })

    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createSupportProgram(
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart("request") @Valid SupportProgramRequest request) throws IOException {
        return ResponseEntity.ok(service.createSupportProgram(thumbnail, request));
    }


    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/save-survey-record")
    public ResponseEntity<?> saveWorking(@Valid @RequestBody CreateSurveyRecordDto dto){
        return ResponseEntity.ok(service.saveSurveySupportProgram(dto));
    }

    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    @PostMapping("/add-participants")
    public ResponseEntity<?> addParticipants(@RequestParam("programId") Integer programId, @RequestParam("listCaseIds") List<Integer> caseIds) {
        return ResponseEntity.ok(service.addParticipantsToSupportProgram(programId, caseIds));
    }

    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity<?> getAllSupportPrograms() {
        return ResponseEntity.ok(service.getAllSupportPrograms());
    }

    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    @GetMapping("/{programId}")
    public ResponseEntity<?> getSupportProgram(@PathVariable Integer programId) {
        return ResponseEntity.ok(service.getSupportProgramById(programId));
    }
}
