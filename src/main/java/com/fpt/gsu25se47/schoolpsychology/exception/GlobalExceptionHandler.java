package com.fpt.gsu25se47.schoolpsychology.exception;


import com.fpt.gsu25se47.schoolpsychology.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or Password is not correct");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleAll(Exception ex) {
//        ex.printStackTrace();
//        return ResponseEntity.status(500).body("Internal Server Error");
//    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicateResource(DuplicateResourceException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error); // 409 Conflict
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<String> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<?>> handleResponseStatusException(ResponseStatusException exception) {
        return switch (exception.getStatusCode()) {
            case HttpStatus.NOT_FOUND -> createResponseEntity(HttpStatus.NOT_FOUND.value(), exception.getReason());
            case HttpStatus.BAD_REQUEST -> createResponseEntity(HttpStatus.BAD_REQUEST.value(), exception.getReason());
            case HttpStatus.INTERNAL_SERVER_ERROR -> createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
            default -> throw new IllegalStateException("Unexpected value: " + exception.getStatusCode());
        };
    }

    private ResponseEntity<ApiResponse<?>> createResponseEntity(int statusCode, String message) {
        return ResponseEntity.status(statusCode)
                .body(ApiResponse.builder()
                        .statusCode(statusCode)
                        .message(message)
                        .success(false)
                        .build());
    }

}
