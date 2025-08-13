package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewSurveyDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSurveyRequest;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/survey")
@Tag(name = "Survey APIs", description = "Quản lý khảo sát - tạo, cập nhật, xem khảo sát")
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    @Operation(summary = "Tạo khảo sát mới", description = "Dành cho Cố vấn hoặc Quản lý. Tạo khảo sát mới với thông tin đầu vào.")
    public ResponseEntity<?> createSurvey(@Valid @RequestBody AddNewSurveyDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(surveyService.addNewSurvey(dto));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    @Operation(summary = "Lấy tất cả khảo sát", description = "Chỉ Quản lý có quyền xem toàn bộ khảo sát.")
    public ResponseEntity<?> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('STUDENT') or hasRole('PARENTS') or hasRole('COUNSELOR')")
    @GetMapping("/{id}")
    @Operation(summary = "Xem chi tiết khảo sát", description = "Xem chi tiết khảo sát theo ID. Các vai trò có thể truy cập: Học sinh, Phụ huynh, Cố vấn, Quản lý.")
    public ResponseEntity<?> getSurveyById(@PathVariable Integer id) {
        return ResponseEntity.ok(surveyService.getSurveyById(id));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật khảo sát", description = "Chỉ Quản lý được quyền cập nhật thông tin khảo sát.")
    public ResponseEntity<?> updateSurvey(@PathVariable Integer id, @Valid @RequestBody UpdateSurveyRequest dto) {
        return ResponseEntity.status(HttpStatus.OK).body(surveyService.updateSurveyById(id, dto));
    }

    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    @GetMapping("/get-by-account")
    @Operation(summary = "Lấy khảo sát theo tài khoản", description = "Lấy danh sách khảo sát mà Cố vấn đã tạo.")
    public ResponseEntity<?> getSurveysByAccount(){
        return ResponseEntity.ok(surveyService.getAllSurveyByCounselorId());
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('PARENTS')")
    @GetMapping("/published")
    @Operation(summary = "Lấy khảo sát đã xuất bản", description = "Dành cho Học sinh và Phụ huynh. Lấy danh sách khảo sát đang được xuất bản.")
    public ResponseEntity<?> getAllSurveyWithPublished(@RequestParam("studentId") Integer studentId) {
        return ResponseEntity.ok(surveyService.getAllSurveyWithPublished(studentId));
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('COUNSELOR') or hasRole('MANAGER') or hasRole('STUDENT') or hasRole('PARENTS')")
    @GetMapping("/in-case")
    @Operation(summary = "Lấy khảo sát liên quan đến Case", description = "Lấy tất cả khảo sát có liên quan đến Case cụ thể (nếu có).")
    public ResponseEntity<?> getAllSurveyStudentInCase(@RequestParam(name = "caseId", required = false) Integer caseId){
        return ResponseEntity.ok(surveyService.getAllSurveyStudentInCase(caseId));
    }
}
