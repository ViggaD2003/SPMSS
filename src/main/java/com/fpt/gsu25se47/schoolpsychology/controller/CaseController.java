package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewCaseDto;
import com.fpt.gsu25se47.schoolpsychology.service.inter.CaseService;
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

    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('COUNSELOR') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<?> getAllCases() {
        return ResponseEntity.ok(caseService.getAllCases());
    }

    @GetMapping("/view-all-by-category")
    public ResponseEntity<?> getAllCasesByCategory(@RequestParam(value = "categoryId") Integer categoryId) {
        return ResponseEntity.ok(caseService.getAllCaseByCategory(categoryId));
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
    public ResponseEntity<?> addCase(@Valid @RequestBody AddNewCaseDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(caseService.createCase(dto));
    }

    @PatchMapping("/assign")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> assignCaseToCounselor(
            @RequestParam Integer counselorId,
            @RequestParam Integer caseId) {
        return ResponseEntity.ok(caseService.assignCounselor(counselorId, caseId));
    }

    @PostMapping("/add-survey")
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    public ResponseEntity<?> addSurveyToCases(
            @RequestParam List<Integer> caseIds,
            @RequestParam Integer surveyId) {
        return ResponseEntity.ok(caseService.addSurveyCaseLink(caseIds, surveyId));
    }

    @PatchMapping("/remove")
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    public ResponseEntity<?> removeSurveyFromCases(
            @RequestParam List<Integer> caseIds) {
        return ResponseEntity.ok(caseService.removeSurveyCaseLink(caseIds));
    }
}
