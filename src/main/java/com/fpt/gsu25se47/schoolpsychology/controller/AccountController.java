package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateProfileDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.StudentDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.TeacherDto;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Quản lý thông tin tài khoản")
public class AccountController {

    private final AccountService accountService;

    @Operation(
            summary = "Lấy thông tin tài khoản hiện tại",
            description = "Trả về thông tin hồ sơ của tài khoản đang đăng nhập theo vai trò: STUDENT, PARENTS, COUNSELOR, hoặc TEACHER"
    )
    @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công")
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('STUDENT') or hasRole('PARENTS') or hasRole('TEACHER')")
    @GetMapping
    public ResponseEntity<?> getProfileAccount() {
        return ResponseEntity.ok(accountService.profileAccount());
    }

    @Operation(
            summary = "Cập nhật thông tin tài khoản",
            description = "Cho phép người dùng cập nhật thông tin cá nhân như họ tên, ngày sinh, số điện thoại,..."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thông tin thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu gửi lên không hợp lệ")
    })
    @PutMapping
    public ResponseEntity<?> updateProfileAccount(@RequestBody UpdateProfileDto updateProfileDto) {
        return ResponseEntity.ok(accountService.updateProfileAccount(updateProfileDto));
    }

    @Operation(
            summary = "Xem danh sách tư vấn viên",
            description = "Chỉ tài khoản có vai trò STUDENT hoặc PARENTS mới được phép gọi API này để xem danh sách tư vấn viên"
    )
    @ApiResponse(responseCode = "200", description = "Lấy danh sách tư vấn viên thành công")
    @PreAuthorize("hasRole('MANAGER') or hasRole('STUDENT') or hasRole('PARENTS')")
    @GetMapping("/view-counselor")
    public ResponseEntity<?> getViewCounselor() {
        return ResponseEntity.ok(accountService.listAllCounselors());
    }

    @Operation(
            summary = "Lấy danh sách tất cả tài khoản",
            description = "Chỉ MANAGER mới được phép truy cập để xem danh sách toàn bộ tài khoản trong hệ thống"
    )
    @ApiResponse(responseCode = "200", description = "Lấy danh sách tài khoản thành công")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/list-all-account")
    public ResponseEntity<?> listAllAccounts(@RequestParam(value = "role", required = false) Role role, @RequestParam(value = "classId", required = false) Integer classId,
            @RequestParam(value = "grade", required = false) Grade grade) {
        return ResponseEntity.ok(accountService.listAllAccounts(role, classId, grade));
    }

    @Operation(
            summary = "Lấy thông tin tài khoản theo ID",
            description = "Chỉ TEACHER hoặc COUNSELOR có thể lấy chi tiết thông tin tài khoản bằng cách truyền vào accountId"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin tài khoản thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy tài khoản với ID đã cho")
    })
    @PreAuthorize("hasRole('MANAGER') or hasRole('COUNSELOR') or hasRole('TEACHER')")
    @GetMapping("/get-account")
    public ResponseEntity<?> getAccount(@RequestParam("accountId") Integer accountId) throws BadRequestException {
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    @Operation(
            summary = "Update isAbleSurvey",
            description = "Chỉ Student và Parent mới được phép cập nhật"
    )
    @ApiResponse(responseCode = "200", description = "Update lại isAbleSurvey thành công ")
    @PreAuthorize("hasRole('MANAGER') or hasRole('STUDENT') or hasRole('PARENTS')")
    @PatchMapping("/update-able-survey/{accountId}")
    public ResponseEntity<?> updateIsAbleSurvey(@RequestParam("isAbleSurvey") Boolean isAbleSurvey, @PathVariable Integer accountId) throws BadRequestException {
        return ResponseEntity.ok(accountService.updateIsAbleSurvey(accountId, isAbleSurvey));
    }

    @GetMapping("/students/eligible")
    ResponseEntity<List<StudentDto>> getEligibleStudents(@RequestParam Integer classId) {

        return ResponseEntity.ok(accountService.getEligibleStudents(classId));
    }

    @GetMapping("/teachers/eligible")
    ResponseEntity<List<TeacherDto>> getEligibleTeachers(@RequestParam Integer classId) {

        return ResponseEntity.ok(accountService.getEligibleTeachers(classId));
    }

    @GetMapping("/students/details")
    public ResponseEntity<?> getStudentDetails(@RequestParam("accountId") Integer accountId) throws BadRequestException {
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }
}
