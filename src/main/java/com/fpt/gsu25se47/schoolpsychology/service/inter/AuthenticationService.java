package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.RefreshTokenRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SignInRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SignUpRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.JwtAuthenticationResponse;
import com.fpt.gsu25se47.schoolpsychology.utils.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<ResponseObject> login(SignInRequest request);

    ResponseEntity<ResponseObject> logout(HttpServletRequest request);

    ResponseEntity<ResponseObject> refresh(RefreshTokenRequest response);

    ResponseEntity<String> signUp(SignUpRequest request);

}
