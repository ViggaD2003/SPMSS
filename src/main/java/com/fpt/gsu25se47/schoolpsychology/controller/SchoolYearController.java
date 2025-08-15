package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.response.SchoolYear.SchoolYearResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SchoolYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/school-years")
@RequiredArgsConstructor
public class SchoolYearController {

    private final SchoolYearService schoolYearService;

    @GetMapping
    ResponseEntity<List<SchoolYearResponse>> getSchoolYears() {

        return ResponseEntity.ok(
                schoolYearService.getSchoolYears());
    }
}
