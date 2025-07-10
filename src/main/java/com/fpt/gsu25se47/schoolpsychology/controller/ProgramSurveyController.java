package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewProgramSurvey;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ProgramSurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/program-survey")
@RequiredArgsConstructor
public class ProgramSurveyController {

    private final ProgramSurveyService programSurveyService;

    @PostMapping
    public ResponseEntity<?> createNewProgramSurvey(@Valid @RequestBody AddNewProgramSurvey programSurvey, @RequestParam(name = "programSupportId") Integer programSupportId) {
        return ResponseEntity.ok(programSurveyService.addNewPrgSurvey(programSurvey, programSupportId));
    }
}
