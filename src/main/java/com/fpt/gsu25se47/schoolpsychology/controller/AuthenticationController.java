package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ChangePasswordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.RefreshTokenRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SignInRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SignUpRequest;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AuthenticationService;
import com.fpt.gsu25se47.schoolpsychology.utils.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> signIn(@Valid @RequestBody SignInRequest request) {
        return authenticationService.login(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseObject> logout(HttpServletRequest request) {
        return authenticationService.logout(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseObject> refresh(@RequestBody RefreshTokenRequest request) {
        return authenticationService.refresh(request);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request, Principal principal) {
        return authenticationService.changePassword(request, principal);
    }
}
