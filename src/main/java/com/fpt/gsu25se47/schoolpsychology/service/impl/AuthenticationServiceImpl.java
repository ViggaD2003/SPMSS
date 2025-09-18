package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.common.GoogleTokenStore;
import com.fpt.gsu25se47.schoolpsychology.common.Status;
import com.fpt.gsu25se47.schoolpsychology.dto.request.*;
import com.fpt.gsu25se47.schoolpsychology.dto.response.JwtAuthenticationResponse;
import com.fpt.gsu25se47.schoolpsychology.exception.DuplicateResourceException;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.EmailTemplateName;
import com.fpt.gsu25se47.schoolpsychology.model.enums.TokenType;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AuthenticationService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.EmailService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.GoogleCalendarService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.JWTService;
import com.fpt.gsu25se47.schoolpsychology.utils.ResponseObject;
import com.fpt.gsu25se47.schoolpsychology.utils.TokenUtil;
import com.google.api.client.http.HttpTransport;
import com.google.api.services.calendar.model.EntryPoint;
import com.google.api.services.calendar.model.Event;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JWTService jwtService;
    private final AccountRepository accountRepo;
    private final TokenRepository tokenRepo;
    private final GoogleTokenStore tokenStore;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final GoogleCalendarService googleCalendarService;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CounselorRepository counselorRepository;
    private final GuardianRepository guardianRepository;
    private final MailTokenRepository mailTokenRepository;
    private final EmailService emailService;

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;


    @Override
    public ResponseEntity<ResponseObject> login(SignInRequest signInRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getEmail(),
                        signInRequest.getPassword()
                )
        );
        Account account = (Account) auth.getPrincipal();

        if (!account.getStatus()) {
            throw new IllegalArgumentException("Account is not active !");
        }

        // ✅ Nếu là ADMIN thì chuyển hướng tới OAuth Google
        if ("MANAGER".equals(account.getRole().name())) {
            String googleAuthUrl = buildGoogleOAuthUrl(account.getEmail()); // phương thức tạo URL
            return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                    .body(ResponseObject.builder()
                            .message("Redirect to Google OAuth")
                            .success(true)
                            .data(googleAuthUrl)
                            .build()
                    );
        }

        // ✅ Nếu không phải ADMIN → tiếp tục flow login như cũ
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

        if (authToken == null) {
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

        if (request.getToken() == null || request.getToken().isBlank()) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message("Refresh token is missing")
                            .success(false)
                            .data(null)
                            .build()
            );
        }

        boolean isExpired;
        try {
            isExpired = jwtService.checkIfExpired(request.getToken());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ResponseObject.builder()
                            .message("Invalid refresh token")
                            .success(false)
                            .data(null)
                            .build()
            );
        }

        if (isExpired) {
            Token accessToken = tokenRepo.findByValue(request.getToken()).orElse(null);
            if (accessToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ResponseObject.builder()
                                .message("Access token not found in database")
                                .success(false)
                                .data(null)
                                .build()
                );
            }

            Token refresh = tokenRepo.findRefreshTokenWithActiveStatusByAccountId(accessToken.getAccount().getId());

            if (refresh == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        ResponseObject.builder()
                                .message("No valid refresh token found")
                                .success(false)
                                .data(null)
                                .build()
                );
            }

            // ✅ Kiểm tra xem refresh token có expired hay không
            boolean refreshExpired;
            try {
                refreshExpired = jwtService.checkIfExpired(refresh.getValue());
            } catch (Exception e) {
                refreshExpired = true;
            }

            if (refreshExpired) {
                // ✅ Cập nhật trạng thái refresh token trong DB
                refresh.setStatus(Status.TOKEN_EXPIRED.getValue());
                tokenRepo.save(refresh);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ResponseObject.builder()
                                .message("Refresh token expired. Please login again.")
                                .success(false)
                                .data(null)
                                .build()
                );
            }

            // ✅ Tạo access token mới
            if (revokeAllActiveAccessTokens(refresh.getAccount())) {
                String newAccess = jwtService.generateAccessToken(refresh.getAccount());
                tokenRepo.save(
                        Token.builder()
                                .value(newAccess)
                                .tokenType(TokenType.ACCESS_TOKEN.getValue())
                                .account(refresh.getAccount())
                                .status(Status.TOKEN_ACTIVE.getValue())
                                .build()
                );

                return ResponseEntity.ok(
                        ResponseObject.builder()
                                .message("Refresh access token successfully")
                                .success(true)
                                .data(JwtAuthenticationResponse.builder().token(newAccess).build())
                                .build()
                );
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        ResponseObject.builder()
                                .message("No valid refresh token found")
                                .success(false)
                                .data(null)
                                .build()
                );
            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseObject.builder()
                            .message("Access token is still valid. No need to refresh.")
                            .success(false)
                            .data(null)
                            .build()
            );
        }
    }


    @Override
    @Transactional
    public ResponseEntity<String> signUp(SignUpRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Sign up request cannot be null");
        }

        if (accountRepo.existsByEmail(request.getEmail())) {
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

        switch (request.getRole()) {
            case TEACHER -> {
                Event event = googleCalendarService.createMeetLinkForTeacher(account.getEmail(), account.getFullName(), account.getRole().name());
                String linkMeet = event.getConferenceData().getEntryPoints().stream()
                        .filter(p -> "video".equals(p.getEntryPointType()))
                        .findFirst()
                        .map(EntryPoint::getUri)
                        .orElse(null);


                Teacher teacher = Teacher.builder()
                        .id(account.getId())
                        .account(account)
                        .teacherCode(generateNextTeacherCode())
                        .linkMeet(linkMeet)
                        .eventId(event.getId())
                        .build();

                teacherRepository.save(teacher);
            }
            case COUNSELOR -> {
                Event event = googleCalendarService.createMeetLinkForTeacher(account.getEmail(), account.getFullName(), account.getRole().name());
                String linkMeet = event.getConferenceData().getEntryPoints().stream()
                        .filter(p -> "video".equals(p.getEntryPointType()))
                        .findFirst()
                        .map(EntryPoint::getUri)
                        .orElse(null);

                Counselor counselor = Counselor.builder()
                        .id(account.getId())
                        .account(account)
                        .linkMeet(linkMeet)
                        .eventId(event.getId())
                        .counselorCode(generateNextCounselorCode())
                        .build();
                counselorRepository.save(counselor);
            }
            case STUDENT -> {
                Student student = Student.builder()
                        .id(account.getId())
                        .account(account)
                        .studentCode(generateNextStudentCode())
                        .isEnableSurvey(true)
                        .build();

                studentRepository.save(student);
            }
            case PARENTS -> {
                Guardian guardian = Guardian.builder()
                        .id(account.getId())
                        .account(account)
                        .build();
                guardianRepository.save(guardian);
            }
        }

        accountRepo.save(account);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        "Sign up successful !"
                );
    }

    @Override
    public ResponseEntity<String> changePassword(ChangePasswordRequest request, Principal connectedUser) {
        try {
            var account = (Account) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

            if (account == null) {
                throw new IllegalArgumentException("Account not found");
            }

            if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
                throw new IllegalArgumentException("Current password doesn't match");
            }

            if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
                throw new IllegalArgumentException("New password doesn't match");
            }

            account.setPassword(passwordEncoder.encode(request.getNewPassword()));
            accountRepo.save(account);

            return ResponseEntity.ok("Change password success !");

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public void callBackGoogleSignIn(String code, HttpServletRequest request, HttpServletResponse response)
            throws GeneralSecurityException, IOException {

        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        // Exchange code for Google token
        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                transport, jsonFactory,
                "https://oauth2.googleapis.com/token",
                clientId, clientSecret, code, redirectUri
        ).execute();

        String googleAccessToken = tokenResponse.getAccessToken();
        String googleRefreshToken = tokenResponse.getRefreshToken();

        System.out.println("???????????????????? LOG ?????????????????????????");
        System.out.println("Access " + googleAccessToken + "\n" + "Refresh " +  googleRefreshToken);
        System.out.println("???????????????????? LOG ?????????????????????????");

        // Save Google tokens
        tokenStore.saveTokens(googleAccessToken, googleRefreshToken);

        // Extract email passed via `state`
        String email = request.getParameter("state");
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Missing email in state parameter");
        }

        Account account = accountRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        revokeAllTokens(account);
        saveTokenAccount(account);

        Token newAccess = getActiveToken(account, TokenType.ACCESS_TOKEN.getValue());
        if (newAccess == null) {
            throw new IllegalStateException("Failed to generate access token");
        }

        response.sendRedirect("https://spmss.vercel.app/login-success?token=" + newAccess.getValue());
    }

    @Override
    public void verifyUserAccount(String email) throws BadRequestException, MessagingException {
        Account account = accountRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Account not found"));
        if(account != null && account.getPassword() == null){
            throw new BadRequestException("This email is not available in our system");
        } else if (account == null) {
            throw new BadRequestException("This email is not available in our system");
        } else {
            List<MailToken> revoking = mailTokenRepository.findAllWhereRevokedIsFalse(account.getId());
            revoking.forEach(t -> t.setRevoked(true));
            mailTokenRepository.saveAll(revoking);
            sendValidationEmail(account);
        }
    }

    @Override
    public String activateChangePassword(String token) throws MessagingException {
        MailToken mailToken = mailTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if(LocalDateTime.now().isAfter(mailToken.getExpiresAt())){
            throw new RuntimeException("Activation token has expired. Please do it again !");
        }

        var user =  accountRepo.findById(mailToken.getAccount().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        mailToken.setValidatedAt(LocalDateTime.now());
        mailTokenRepository.save(mailToken);

        return "Verify Successfully !";
    }

    @Override
    public String changeForgotPassword(String email, ChangeForgotPasswordRequest request) {
        Account account = accountRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(!request.getNewPassword().equals(request.getConfirmNewPassword())){
            throw new IllegalArgumentException("password are not the same");
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepo.save(account);
        return "Change Password Successfully !";
    }


    private String generateAndSaveActivationToken(Account account) {
        // Generate a token
        String generatedToken = generateActivationCode(6);
        var token = MailToken.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .revoked(false)
                .account(account)
                .build();
        mailTokenRepository.save(token);
        return generatedToken;
    }

    private void sendValidationEmail(Account account) throws MessagingException {
        var newToken = generateAndSaveActivationToken(account);

        emailService.sendEmail(
                account.getEmail(),
                account.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                "",
                newToken,
                "OTP CODE"
        );
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }


    private void revokeAllTokens(Account account) {
        List<Token> tokens = tokenRepo.findAllByAccount_IdAndStatus(account.getId(), Status.TOKEN_ACTIVE.getValue());
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

    private boolean revokeAllActiveAccessTokens(Account account) {
        List<Token> tokens = tokenRepo.findAllByTokenTypeAndStatusAndAccount_Id(TokenType.ACCESS_TOKEN.getValue(), Status.TOKEN_ACTIVE.getValue(), account.getId());
        for (Token t : tokens) {
            TokenUtil.handleExpiredToken(t.getValue(), tokenRepo);
        }
        return true;
    }

    private String buildGoogleOAuthUrl(String email) {
        return "https://accounts.google.com/o/oauth2/auth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=https://www.googleapis.com/auth/calendar"
                + "&access_type=offline"
                + "&prompt=consent"
                + "&state=" + email;
    }


    private String generateNextTeacherCode() {
        String lastCode = teacherRepository.findTopTeacherCode();
        int nextNumber = 1;

        if (lastCode != null && lastCode.matches("TC\\d+")) {
            nextNumber = Integer.parseInt(lastCode.substring(2)) + 1;
        }

        return String.format("TC%04d", nextNumber);
    }

    private String generateNextCounselorCode() {
        String lastCode = counselorRepository.findTopCounselorCode();
        int nextNumber = 1;

        if (lastCode != null && lastCode.matches("CL\\d+")) {
            nextNumber = Integer.parseInt(lastCode.substring(2)) + 1;
        }

        return String.format("CL%04d", nextNumber);
    }

    private String generateNextStudentCode() {
        String lastCode = studentRepository.findTopStudentCode();
        int nextNumber = 1;

        if (lastCode != null && lastCode.matches("ST\\d+")) {
            nextNumber = Integer.parseInt(lastCode.substring(2)) + 1;
        }

        return String.format("ST%04d", nextNumber);
    }
}
