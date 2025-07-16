package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.common.ApiResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.request.ProgramRegistrationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ProgramRegistrationResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ProgramRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/program-registrations")
@RequiredArgsConstructor
public class ProgramRegistrationController {

    private final ProgramRegistrationService programRegistrationService;

    @Operation(
            summary = "Create a new program registration",
            description = "Create a new program registration with status: \n" +
                    "REGISTERED,\n" +
                    "    CHECKIN,\n" +
                    "    CHECKOUT,\n" +
                    "    COMPLETED" +
                    "Role: ALL"
    )
    @PostMapping
    ResponseEntity<ApiResponse<ProgramRegistrationResponse>> createProgramRegistration(@RequestBody ProgramRegistrationRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<ProgramRegistrationResponse>builder()
                        .success(true)
                        .message("Program registration created successfully")
                        .statusCode(HttpStatus.OK.value())
                        .data(programRegistrationService.createProgramRegistration(request))
                        .build()
        );
    }

    @Operation(
            summary = "Get all program registrations by programID",
            description = "Get all program registrations by programID"
    )
    @GetMapping("/programs/{id}")
    ResponseEntity<ApiResponse<List<ProgramRegistrationResponse>>> getAllByProgramId(@PathVariable Integer id) {

        return ResponseEntity.ok(
                ApiResponse.<List<ProgramRegistrationResponse>>builder()
                        .success(true)
                        .message("Program registrations retrieved successfully")
                        .statusCode(HttpStatus.OK.value())
                        .data(programRegistrationService.getAllProgramRegistrationsByProgramId(id))
                        .build()
        );
    }

    @Operation(
            summary = "Get all program registrations by accountId",
            description = "Get all program registrations by accountId"
    )
    @GetMapping("/accounts/{id}")
    ResponseEntity<ApiResponse<List<ProgramRegistrationResponse>>> getAllByAccountId(@PathVariable Integer id) {

        return ResponseEntity.ok(
                ApiResponse.<List<ProgramRegistrationResponse>>builder()
                        .success(true)
                        .message("Program registrations retrieved successfully")
                        .statusCode(HttpStatus.OK.value())
                        .data(programRegistrationService.getAllProgramRegistrationsByAccountId(id))
                        .build()
        );
    }

    @Operation(
            summary = "Get all program registrations by programRegistrationId",
            description = "Get all program registrations by programRegistrationId"
    )
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<ProgramRegistrationResponse>> getById(@PathVariable Integer id) {

        return ResponseEntity.ok(
                ApiResponse.<ProgramRegistrationResponse>builder()
                        .success(true)
                        .message("Program registration retrieved successfully")
                        .statusCode(HttpStatus.OK.value())
                        .data(programRegistrationService.getById(id))
                        .build()
        );
    }

    @Operation(
            summary = "Update program registration by programRegistrationId",
            description = "Update program registration by programRegistrationId\n" +
                    "status: \n" +
                    "REGISTERED,\n" +
                    "    CHECKIN,\n" +
                    "    CHECKOUT,\n" +
                    "    COMPLETED"
    )
    @PutMapping
    ResponseEntity<ApiResponse<ProgramRegistrationResponse>> update(
            @RequestParam Integer programRegistrationId,
            @RequestBody ProgramRegistrationRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<ProgramRegistrationResponse>builder()
                        .success(true)
                        .message("Program registration updated successfully")
                        .statusCode(HttpStatus.OK.value())
                        .data(programRegistrationService.updateProgramRegistration(programRegistrationId, request))
                        .build()
        );
    }
}
