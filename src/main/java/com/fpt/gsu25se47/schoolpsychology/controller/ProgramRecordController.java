package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateProgramRecordDto;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ProgramSurveyRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/program-record")
@RequiredArgsConstructor
public class ProgramRecordController {

    private final ProgramSurveyRecordService programSurveyRecordService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitProgramSurveyRecord(@Valid @RequestBody CreateProgramRecordDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(programSurveyRecordService.submitProgramRecord(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getAllProgramSurveyRecords(@RequestParam Integer programSurveyId, @RequestParam Integer registerId){
        return ResponseEntity.ok(programSurveyRecordService.showProgramRecord(programSurveyId, registerId));
    }

}
