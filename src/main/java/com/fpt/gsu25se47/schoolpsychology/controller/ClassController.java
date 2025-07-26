package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateEnrollmentRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.EnrollmentResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ClassService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;
    private final EnrollmentService enrollmentService;

    @PostMapping
    ResponseEntity<ClassResponse> createClass(@RequestBody CreateClassRequest request) {

        return ResponseEntity.ok(classService.createClass(request));
    }

    @PostMapping("/enrollments")
    ResponseEntity<List<EnrollmentResponse>> createEnrollmentsForClass(@RequestBody CreateEnrollmentRequest request) {

        return ResponseEntity.ok(enrollmentService.createBulkEnrollment(request));
    }
}