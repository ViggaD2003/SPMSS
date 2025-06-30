package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateProfileDto;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<?> getProfileAccount() {
        return ResponseEntity.ok(accountService.profileAccount());
    }

    @PutMapping
    public ResponseEntity<?> updateProfileAccount(@RequestBody UpdateProfileDto updateProfileDto) {
        return ResponseEntity.ok(accountService.updateProfileAccount(updateProfileDto));
    }
}
