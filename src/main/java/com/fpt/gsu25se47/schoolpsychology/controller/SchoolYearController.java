package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSchoolYearRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SchoolYear.SchoolYearResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SchoolYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    ResponseEntity<SchoolYearResponse> createSchoolYear(@RequestBody CreateSchoolYearRequest request) {

        return ResponseEntity.ok(
                schoolYearService.createSchoolYear(request)
        );
    }
}
