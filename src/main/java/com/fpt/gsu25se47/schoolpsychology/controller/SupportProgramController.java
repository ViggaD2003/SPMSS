package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.RegisterProgramParticipantResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramPPResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramStudentDetail;
import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramStatus;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SupportProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
            description = """
                    Create a new support program with details such as name, schedule, participants, and category.
                    This does not include session creation. Only users with roles COUNSELOR or MANAGER can access.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Support program created successfully",
                    content = @Content(schema = @Schema(implementation = SupportProgramResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createSupportProgram(
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart("request") @Valid SupportProgramRequest request) throws IOException {
        return ResponseEntity.ok(service.createSupportProgram(thumbnail, request));
    }

    @Operation(
            summary = "Save a student's survey record",
            description = """
                    Allows a student to submit their completed survey record.
                    Used to support mental health program recommendations.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Survey record saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/save-survey-record")
    public ResponseEntity<?> saveWorking(@RequestParam("programId") Integer programId,@RequestParam("studentId") Integer studentId, @Valid @RequestBody CreateSurveyRecordDto dto) {
        return ResponseEntity.ok(service.saveSurveySupportProgram(programId, studentId, dto));
    }

    @Operation(
            summary = "Add participants to a support program",
            description = """
                    Adds a list of case IDs (students) to an existing support program.
                    Accessible only to users with roles COUNSELOR or MANAGER.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participants added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Program or cases not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    @PostMapping("/add-participants")
    public ResponseEntity<?> addParticipants(
            @RequestParam("programId") Integer programId,
            @RequestParam("listCaseIds") List<Integer> caseIds) {
        return ResponseEntity.ok(service.addParticipantsToSupportProgram(programId, caseIds));
    }

    @Operation(
            summary = "Get all support programs",
            description = "Fetches all available support programs. Only accessible to COUNSELOR and MANAGER roles.")
    @ApiResponse(responseCode = "200", description = "List of support programs retrieved successfully")
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity<?> getAllSupportPrograms() {
        return ResponseEntity.ok(service.getAllSupportPrograms().reversed());
    }

    @Operation(
            summary = "Get details of a support program by ID",
            description = "Retrieves full information about a specific support program using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Program details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Program not found")
    })
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    @GetMapping("/{programId}")
    public ResponseEntity<?> getSupportProgram(@PathVariable Integer programId) {
        return ResponseEntity.ok(service.getSupportProgramById(programId));
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('PARENTS')")
    @GetMapping("/participant-program-detail")
    public ResponseEntity<SupportProgramStudentDetail> getParticipantProgramDetail(@RequestParam("studentId") Integer studentId, @RequestParam("programId") Integer programId) {
        return ResponseEntity.ok(service.getSupportProgramStudentDetailById(programId, studentId));
    }

    @Operation(
            summary = "Update status of a support program",
            description = "Updates the status (e.g. ACTIVE, INACTIVE) of a support program by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "404", description = "Program not found")
    })
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatusSupportProgram(@PathVariable Integer id, @RequestParam ProgramStatus status) {
        return ResponseEntity.ok(service.updateStatusSupportProgram(id, status));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}/update-program")
    public ResponseEntity<?> updateSupportProgram(@PathVariable Integer id, @RequestBody UpdateSupportProgramRequest request) {
        return ResponseEntity.ok(service.updateSupportProgram(id, request));
    }

    @Operation(
            summary = "Register current student to a support program",
            description = """
                    Allows a logged-in student to register themselves for a selected support program.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student registered successfully",
                    content = @Content(schema = @Schema(implementation = RegisterProgramParticipantResponse.class))),
            @ApiResponse(responseCode = "404", description = "Program not found"),
            @ApiResponse(responseCode = "400", description = "Invalid registration request")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/participants/register")
    ResponseEntity<RegisterProgramParticipantResponse> registerParticipantsToProgram(
            @RequestParam Integer programId) {
        return ResponseEntity.ok(service.registerStudentToSupportProgram(programId));
    }

    @Operation(
            summary = "View recommended support programs for a student",
            description = "Returns a list of recommended programs based on student's mental health status or survey results.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recommendations retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/recommend")
    public ResponseEntity<?> viewRecommendSupportProgram(@RequestParam("studentId") Integer studentId) {
        return ResponseEntity.ok(service.getSuggestSupportProgram(studentId).reversed());
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/participants/unregister")
    public ResponseEntity<String> unregisterStudentFromSupportProgram(@RequestParam("supportProgramId") Integer supportProgramId, @RequestParam("studentId") Integer studentId) {
        return ResponseEntity.ok(service.unRegisterStudentFromSupportProgram(supportProgramId, studentId));
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('PARENTS')")
    @GetMapping("/participants")
    public ResponseEntity<List<SupportProgramPPResponse>> findByStudentId(@RequestParam("studentId") Integer studentId) {
        return ResponseEntity.ok(service.getSupportProgramsByStudentId(studentId).reversed());
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('COUNSELOR')")
    @PatchMapping("/open-survey")
    public ResponseEntity<String> openSurvey(@RequestParam("supportProgramId") Integer supportProgramId) {
        return ResponseEntity.ok(service.openSurvey(supportProgramId));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/active-program")
    public ResponseEntity<List<SupportProgramResponse>> findAllActiveProgram() {
        return ResponseEntity.ok(service.getAllActiveSupportPrograms());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping(value = "/add-new-thumbnail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addNewThumbnail(
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart("programId") Integer programId
    )  throws IOException {
        return ResponseEntity.ok(service.addNewThumbnail(programId, thumbnail));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping(value = "/delete-thumbnail")
    public ResponseEntity<?> deleteThumbnail(
            @RequestParam("programId") Integer programId,
            @RequestParam("publicId") String publicId
    ) {
        return ResponseEntity.ok(service.deleteThumbnail(programId, publicId));
    }
}