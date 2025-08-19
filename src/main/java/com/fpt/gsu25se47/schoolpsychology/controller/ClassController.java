package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateEnrollmentRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Classes.ClassResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Classes.ClassResponseSRC;
import com.fpt.gsu25se47.schoolpsychology.dto.response.EnrollmentResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ClassService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.EnrollmentService;
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

//    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    ResponseEntity<List<ClassResponse>> createClass(@RequestBody @Valid List<CreateClassRequest> requests) {
        return ResponseEntity.ok(classService.createClass(requests));
    }

    @PostMapping("/enrollments")
    ResponseEntity<List<EnrollmentResponse>> createEnrollmentsForClass(@RequestBody CreateEnrollmentRequest request) {

        return ResponseEntity.ok(enrollmentService.createBulkEnrollment(request));
    }

    @GetMapping
    ResponseEntity<List<ClassResponse>> findAll() {
        return ResponseEntity.ok(classService.getAllClasses().reversed());
    }

//    @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
//    @GetMapping("/{classId}")
//    ResponseEntity<ClassResponseSRC> findById(@PathVariable Integer classId) {
//
//        return ResponseEntity.ok(classService.getClassById(classId));
//    }

    @GetMapping("/code/{code}")
    ResponseEntity<ClassResponseSRC> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(classService.getClassByCode(code));
    }

    @PutMapping
    ResponseEntity<ClassResponse> updateClass(
            @RequestParam Integer classId,
            @RequestBody UpdateClassRequest request) {

        return ResponseEntity.ok(classService.updateClass(classId, request));
    }

    @GetMapping("/teacher/{teacherId}")
    ResponseEntity<List<ClassResponse>> getAllByTeacherId(@PathVariable Integer teacherId) {

        return ResponseEntity.ok(classService.getClassesByTeacherId(teacherId));
    }
}