package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.common.ApiResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateMentalEvaluationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.MentalEvaluationResponse;
import com.fpt.gsu25se47.schoolpsychology.model.enums.EvaluationType;
import com.fpt.gsu25se47.schoolpsychology.service.inter.MentalEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mental-evaluations")
public class MentalEvaluationController {

    private final MentalEvaluationService mentalEvaluationService;

    @PostMapping
    public ResponseEntity<ApiResponse<MentalEvaluationResponse>> createMentalEvaluation(@RequestBody CreateMentalEvaluationRequest request) {

        return ResponseEntity.ok(ApiResponse.<MentalEvaluationResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .data(mentalEvaluationService.createMentalEvaluation(request))
                .message("Create mental evaluation successfully")
                .build());
    }

    @GetMapping("/{mentalEvaluationId}")
    public ResponseEntity<ApiResponse<MentalEvaluationResponse>> getMentalEvaluationById(@PathVariable int mentalEvaluationId) {

        return ResponseEntity.ok(ApiResponse.<MentalEvaluationResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .data(mentalEvaluationService.getMentalEvaluationById(mentalEvaluationId))
                .message("Retrieve mental evaluation by Id: " + mentalEvaluationId + " successfully")
                .build());
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<ApiResponse<List<MentalEvaluationResponse>>> getAllMentalEvaluationsByStudentId(
            @PathVariable int studentId,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false) EvaluationType evaluationType,
            @RequestParam(defaultValue = "date") String field,
            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(ApiResponse.<List<MentalEvaluationResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .data(mentalEvaluationService.getMentalEvaluationsByAccountId(studentId, from, to, evaluationType, field, direction))
                        .message("Retrieved list of mental evaluations by studentId: " + studentId + " successfully")
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MentalEvaluationResponse>>> getAllMentalEvaluations(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false) EvaluationType evaluationType,
            @RequestParam(defaultValue = "date") String field,
            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(ApiResponse.<List<MentalEvaluationResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .data(mentalEvaluationService.getAllMentalEvaluations(from, to, evaluationType, field, direction))
                .message("Retrieved list of mental evaluations successfully")
                .build());
    }
}
