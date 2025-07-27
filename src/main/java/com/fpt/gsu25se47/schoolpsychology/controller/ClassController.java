package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateEnrollmentRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.EnrollmentResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ClassService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;
    private final EnrollmentService enrollmentService;

    @PreAuthorize("hasRole('MANAGER')")
    @Operation(description = "schoolYear = 'startYear-endYear', ex: '2021-2024'")
    @PostMapping
    ResponseEntity<List<ClassResponse>> createClass(@RequestBody @Valid List<CreateClassRequest> requests) {

        return ResponseEntity.ok(classService.createClass(requests));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/enrollments")
    ResponseEntity<List<EnrollmentResponse>> createEnrollmentsForClass(@RequestBody CreateEnrollmentRequest request) {

        return ResponseEntity.ok(enrollmentService.createBulkEnrollment(request));
    }

    @GetMapping
    ResponseEntity<List<ClassResponse>> findAll() {

        return ResponseEntity.ok(classService.getAllClasses());
    }

    @GetMapping("/{classId}")
    ResponseEntity<ClassResponse> findById(@PathVariable Integer classId) {

        return ResponseEntity.ok(classService.getClassById(classId));
    }

    @GetMapping("/code/{code}")
    ResponseEntity<ClassResponse> findByCode(@PathVariable String code) {

        return ResponseEntity.ok(classService.getClassByCode(code));
    }
}