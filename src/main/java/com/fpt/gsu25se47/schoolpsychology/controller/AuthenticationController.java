package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AuthenticationService;
import com.fpt.gsu25se47.schoolpsychology.utils.ResponseObject;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('MANAGER')")
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

    @PostMapping("/change-forgot-password")
    public ResponseEntity<?> changeForgotPassword(@RequestBody ChangeForgotPasswordRequest request, @RequestParam String email) throws MessagingException {
        return ResponseEntity.ok(authenticationService.changeForgotPassword(email, request));
    }

    @PostMapping("/activate-email")
    public ResponseEntity<?> confirmEmail( @RequestParam String token
    ) throws MessagingException {
        return ResponseEntity.ok(authenticationService.activateChangePassword(token));
    }


    @PostMapping("/verify-email")
    public void verifyEmail( @RequestParam String email) throws MessagingException, BadRequestException {
        authenticationService.verifyUserAccount(email);
    }

}
