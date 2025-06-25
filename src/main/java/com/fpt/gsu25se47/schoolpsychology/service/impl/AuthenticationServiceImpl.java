package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.common.Status;
import com.fpt.gsu25se47.schoolpsychology.dto.request.RefreshTokenRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SignInRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SignUpRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.JwtAuthenticationResponse;
import com.fpt.gsu25se47.schoolpsychology.exception.DuplicateResourceException;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Token;
import com.fpt.gsu25se47.schoolpsychology.model.enums.TokenType;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.TokenRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AuthenticationService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.JWTService;
import com.fpt.gsu25se47.schoolpsychology.utils.ResponseObject;
import com.fpt.gsu25se47.schoolpsychology.utils.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JWTService jwtService;
    private final AccountRepository accountRepo;
    private final TokenRepository tokenRepo;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;


    @Override
    public ResponseEntity<ResponseObject> login(SignInRequest signInRequest) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
        Account account = (Account) auth.getPrincipal();
        if (!account.getStatus()) {
            throw new IllegalArgumentException("Account is not active !");
        }
        revokeAllTokens(account);
        saveTokenAccount(account);

        Token newAccess = getActiveToken(account, TokenType.ACCESS_TOKEN.getValue());
        Token newRefresh = getActiveToken(account, TokenType.REFRESH_TOKEN.getValue());

        assert newAccess != null;
        assert newRefresh != null;

        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Login successfully")
                        .success(true)
                        .data(JwtAuthenticationResponse.builder()
                                .token(newAccess.getValue())
                                .build())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ResponseObject> logout(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization");

        String accessToken = authToken.substring(7);

        if (authToken == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    ResponseObject.builder()
                            .message("Logout failed")
                            .success(false)
                            .data(null)
                            .build()
            );
        }

        Token token = tokenRepo.findByValue(accessToken).orElse(null);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    ResponseObject.builder()
                            .message("Token invalid")
                            .success(false)
                            .data(null)
                            .build()
            );
        }

        revokeAllTokens(token.getAccount());
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Logout successfully")
                        .success(true)
                        .data(null)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ResponseObject> refresh(RefreshTokenRequest request) {
        if(request.getToken() != null && jwtService.checkIfNotExpired(request.getToken())){
            Token refresh = tokenRepo.findByValue(request.getToken()).orElse(null);
            if(refresh != null && revokeAllActiveAccessTokens(refresh.getAccount())) {
                String newAccess = jwtService.generateAccessToken(refresh.getAccount());
                tokenRepo.save(
                        Token.builder()
                                .value(newAccess)
                                .tokenType(TokenType.ACCESS_TOKEN.getValue())
                                .account(refresh.getAccount())
                                .status(Status.TOKEN_ACTIVE.getValue())
                                .build()
                );

                return ResponseEntity.status(HttpStatus.OK).body(
                        ResponseObject.builder()
                                .message("Refresh access token successfully")
                                .success(true)
                                .data(JwtAuthenticationResponse.builder()
                                        .token(newAccess)
                                        .build())
                                .build()
                );
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    ResponseObject.builder()
                            .message("No refresh token found")
                            .success(false)
                            .data(null)
                            .build());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ResponseObject.builder()
                        .message("Refresh invalid")
                        .success(false)
                        .data(null)
                        .build()
        );
    }

    @Override
    public ResponseEntity<String> signUp(SignUpRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Sign up request cannot be null");
        }

        if(accountRepo.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .dob(request.getDob())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .status(true)
                .build();

        accountRepo.save(account);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        "Sign up successful !"
                );
    }


    private void revokeAllTokens(Account account) {
        List<Token> tokens = tokenRepo.findAllByAccount_Id(account.getId());
        for (Token token : tokens) {
            TokenUtil.handleExpiredToken(token.getValue(), tokenRepo);
        }
    }

    private void saveTokenAccount(Account account) {
        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);
        tokenRepo.save(
                Token.builder()
                        .value(accessToken)
                        .status(Status.TOKEN_ACTIVE.getValue())
                        .account(account)
                        .tokenType(TokenType.ACCESS_TOKEN.getValue())
                        .build()
        );

        tokenRepo.save(
                Token.builder()
                        .value(refreshToken)
                        .status(Status.TOKEN_ACTIVE.getValue())
                        .account(account)
                        .tokenType(TokenType.REFRESH_TOKEN.getValue())
                        .build()
        );
    }

    private Token getActiveToken(Account account, String type) {
        List<Token> tokens = tokenRepo.findAllByTokenTypeAndStatusAndAccount_Id(type, Status.TOKEN_ACTIVE.getValue(), account.getId());
        if (!tokens.isEmpty()) {
            if (tokens.size() > 1) {
                for (int i = 0; i < tokens.size() - 1; i++) {
                    TokenUtil.handleExpiredToken(tokens.get(i).getValue(), tokenRepo);
                }
            }
            return tokens.getLast();
        }
        return null;
    }

    private boolean revokeAllActiveAccessTokens(Account account){
        List<Token> tokens = tokenRepo.findAllByTokenTypeAndStatusAndAccount_Id(TokenType.ACCESS_TOKEN.getValue(), Status.TOKEN_ACTIVE.getValue(), account.getId());
        for(Token t: tokens){
            TokenUtil.handleExpiredToken(t.getValue(), tokenRepo);
        }
        return true;
    }

}
