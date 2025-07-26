package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewSurveyDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSurveyRequest;
import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/survey")
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    public ResponseEntity<?> createSurvey(@Valid @RequestBody AddNewSurveyDto dto, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(surveyService.addNewSurvey(dto, request));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity<?> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('STUDENT') or hasRole('PARENTS') or hasRole('COUNSELOR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getSurveyById(@PathVariable Integer id) {
        return ResponseEntity.ok(surveyService.getSurveyById(id));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSurvey(@PathVariable Integer id, @Valid @RequestBody UpdateSurveyRequest dto) {
        return ResponseEntity.status(HttpStatus.OK).body(surveyService.updateSurveyById(id, dto));
    }

    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    @GetMapping("/get-by-account")
    public ResponseEntity<?> getSurveysByAccount(){
        return ResponseEntity.ok(surveyService.getAllSurveyByCounselorId());
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('PARENTS')")
    @GetMapping("/published")
    public ResponseEntity<?> getAllSurveyWithPublished(){
        return ResponseEntity.ok(surveyService.getAllSurveyWithPublished());
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('PARENTS')")
    @GetMapping("/in-case")
    public ResponseEntity<?> getAllSurveyStudentInCase(){
        return ResponseEntity.ok(surveyService.getAllSurveyStudentInCase());
    }
}
