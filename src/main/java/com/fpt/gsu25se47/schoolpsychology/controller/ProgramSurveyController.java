package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewProgramSurvey;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ProgramSurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/program-survey")
@RequiredArgsConstructor
public class ProgramSurveyController {

    private final ProgramSurveyService programSurveyService;

    @Operation(summary = "Tạo mới chương trình khảo sát", description = "Chỉ MANAGER có quyền tạo khảo sát mới cho chương trình hỗ trợ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Khảo sát được tạo thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> createNewProgramSurvey(
            @Valid @RequestBody AddNewProgramSurvey programSurvey,
            @Parameter(description = "ID của chương trình hỗ trợ", required = true)
            @RequestParam(name = "supportProgramId") Integer supportProgramId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(programSurveyService.addNewPrgSurvey(programSurvey, supportProgramId));
    }

    @Operation(summary = "Lấy danh sách khảo sát", description = "MANAGER và COUNSELOR có thể xem danh sách khảo sát của chương trình hỗ trợ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy danh sách khảo sát thành công"),
            @ApiResponse(responseCode = "400", description = "Tham số không hợp lệ", content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('COUNSELOR')")
    public ResponseEntity<?> getAllProgramSurveys(
            @Parameter(description = "ID của chương trình hỗ trợ", required = true)
            @RequestParam(name = "supportProgramId") Integer supportProgramId) {
        return ResponseEntity.ok(programSurveyService.getAllPrgSurvey(supportProgramId));
    }

    @Operation(summary = "Cập nhật khảo sát", description = "Chỉ MANAGER có quyền cập nhật khảo sát")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ", content = @Content),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khảo sát", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> updateProgramSurvey(
            @Parameter(description = "ID của khảo sát cần cập nhật", required = true)
            @PathVariable("id") Integer programSurveyId,
            @Valid @RequestBody AddNewProgramSurvey addNewProgramSurvey) {
        return ResponseEntity.ok(programSurveyService.updatePrgSurvey(addNewProgramSurvey, programSurveyId));
    }
}
