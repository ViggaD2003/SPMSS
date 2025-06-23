package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewSurveyDto;
import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/survey")
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    @PreAuthorize("hasRole('COUNSELOR')")
    public ResponseEntity<?> createSurvey(@Valid @RequestBody AddNewSurveyDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(surveyService.addNewSurvey(dto));
    }
}
