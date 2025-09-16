package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.*;
import com.fpt.gsu25se47.schoolpsychology.utils.ResponseObject;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;

public interface AuthenticationService {
    ResponseEntity<ResponseObject> login(SignInRequest request);

    ResponseEntity<ResponseObject> logout(HttpServletRequest request);

    ResponseEntity<ResponseObject> refresh(RefreshTokenRequest response);

    ResponseEntity<String> signUp(SignUpRequest request);

    ResponseEntity<String> changePassword(ChangePasswordRequest request, Principal connectedAccount);

    void callBackGoogleSignIn(String code, HttpServletRequest request,  HttpServletResponse response) throws GeneralSecurityException, IOException;

    void verifyUserAccount(String email) throws MessagingException, BadRequestException;
    String activateChangePassword(String token) throws MessagingException;
    String changeForgotPassword(String email, ChangeForgotPasswordRequest request) throws MessagingException;
}
