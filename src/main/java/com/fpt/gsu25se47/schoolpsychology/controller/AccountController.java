package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateProfileDto;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Quản lý thông tin tài khoản")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Lấy thông tin tài khoản hiện tại")
    @ApiResponse(responseCode = "200", description = "Lấy thành công")
    @GetMapping
    public ResponseEntity<?> getProfileAccount() {
        return ResponseEntity.ok(accountService.profileAccount());
    }

    @Operation(summary = "Cập nhật thông tin tài khoản")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    })
    @PutMapping
    public ResponseEntity<?> updateProfileAccount(@RequestBody UpdateProfileDto updateProfileDto) {
        return ResponseEntity.ok(accountService.updateProfileAccount(updateProfileDto));
    }

    @Operation(summary = "Xem danh sách tư vấn viên", description = "Chỉ STUDENT hoặc PARENTS mới được truy cập")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @PreAuthorize("hasRole('STUDENT') or hasRole('PARENTS')")
    @GetMapping("/view-counselor")
    public ResponseEntity<?> getViewCounselor() {
        return ResponseEntity.ok(accountService.listAllCounselors());
    }
}
