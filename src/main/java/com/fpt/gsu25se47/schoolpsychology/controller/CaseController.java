package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewCaseDto;
import com.fpt.gsu25se47.schoolpsychology.service.inter.CaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cases")
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;

    @Operation(summary = "Get all cases", description = "Retrieve all cases based on status and/or category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cases retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('COUNSELOR') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<?> getAllCases(
            @Parameter(description = "List of status to filter cases", required = false)
            @RequestParam(required = false) List<String> statusCase,
            @Parameter(description = "Category ID to filter cases", required = false)
            @RequestParam(name = "categoryId", required = false) Integer categoryId) {
        return ResponseEntity.ok(caseService.getAllCases(statusCase, categoryId));
    }

    @Operation(summary = "Get all cases by category", description = "Retrieve all cases that belong to a specific category")
    @GetMapping("/view-all-by-category")
    public ResponseEntity<?> getAllCasesByCategory(
            @Parameter(description = "Category ID") @RequestParam("categoryId") Integer categoryId) {
        return ResponseEntity.ok(caseService.getAllCaseByCategory(categoryId));
    }

    @Operation(summary = "Create new case", description = "Add a new case to the system")
    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
    public ResponseEntity<?> addCase(
            @Parameter(description = "New case information") @Valid @RequestBody AddNewCaseDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(caseService.createCase(dto));
    }

    @Operation(summary = "Assign counselor", description = "Assign a counselor to a specific case")
    @PatchMapping("/assign")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> assignCaseToCounselor(
            @Parameter(description = "Counselor ID") @RequestParam Integer counselorId,
            @Parameter(description = "Case ID") @RequestParam Integer caseId) {
        return ResponseEntity.ok(caseService.assignCounselor(counselorId, caseId));
    }

    @Operation(summary = "Add survey to multiple cases", description = "Link a survey to multiple cases")
    @PostMapping("/add-survey")
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    public ResponseEntity<?> addSurveyToCases(
            @Parameter(description = "List of case IDs") @RequestParam List<Integer> caseIds,
            @Parameter(description = "Survey ID") @RequestParam Integer surveyId) {
        return ResponseEntity.ok(caseService.addSurveyCaseLink(caseIds, surveyId));
    }

    @Operation(summary = "Remove survey from cases", description = "Unlink surveys from the specified cases")
    @PatchMapping("/remove")
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    public ResponseEntity<?> removeSurveyFromCases(
            @Parameter(description = "List of case IDs to remove surveys from") @RequestParam List<Integer> caseIds) {
        return ResponseEntity.ok(caseService.removeSurveyCaseLink(caseIds));
    }

    @Operation(summary = "Get case by ID", description = "Retrieve detailed information for a specific case")
    @GetMapping("/{caseId}")
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('TEACHER') or hasRole('MANAGER')")
    public ResponseEntity<?> getCaseById(
            @Parameter(description = "ID of the case") @PathVariable Integer caseId) {
        return ResponseEntity.ok(caseService.getDetailById(caseId));
    }
}
