package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.common.ApiResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.request.ClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassDto;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/classes")
public class ClassController {

    private final ClassService classService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tạo một lớp mới", description = "Tạo một lớp mới, có thể tạo một lớp trống bằng cách bỏ trống dữ liệu list student codes," +
            " hoặc thêm list học sinh dựa vào studentCode")
    public ResponseEntity<ApiResponse<ClassDto>> createClass(@RequestBody ClassRequest request) {

        ClassDto classCreated = classService.createClass(request);

        return ResponseEntity.ok(ApiResponse.<ClassDto>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Created class successfully")
                .data(classCreated)
                .build());
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<ClassDto>> getClassByCode(@PathVariable String code) {

        return ResponseEntity.ok(ApiResponse.<ClassDto>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Retrieved Class successfully")
                .data(classService.getClassByCode(code))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClassDto>>> getAllClasses() {

        return ResponseEntity.ok(ApiResponse.<List<ClassDto>>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Retrieved List of classes successfully")
                .data(classService.getAllClasses())
                .build());
    }

    @DeleteMapping("/delete/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteClassByCode(@PathVariable String code) {

        classService.deleteClassByCode(code);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .success(true)
                .message("Class deleted successfully")
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @PatchMapping("/update/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClassDto>> updateClass(@PathVariable String code,
                                                             @RequestBody ClassRequest request) {

        return ResponseEntity.ok(ApiResponse.<ClassDto>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Created class successfully")
                .data(classService.updateClass(code, request))
                .build());
    }
}
