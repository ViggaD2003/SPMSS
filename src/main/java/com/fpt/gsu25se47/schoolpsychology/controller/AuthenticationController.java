package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ChangePasswordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.RefreshTokenRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SignInRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SignUpRequest;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AuthenticationService;
import com.fpt.gsu25se47.schoolpsychology.utils.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
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

    @GetMapping("/oauth/callback")
    public void googleCallBack(@RequestParam String code, HttpServletRequest request,  HttpServletResponse response) throws GeneralSecurityException, IOException {
        authenticationService.callBackGoogleSignIn(code, request,response);
    }

}
