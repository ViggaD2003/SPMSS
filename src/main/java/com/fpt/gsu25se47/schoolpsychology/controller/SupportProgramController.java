package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.common.ApiResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SupportProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/support-programs")
@RequiredArgsConstructor
public class SupportProgramController {

    private final SupportProgramService service;

    @Operation(
            summary = "Create a new support program",
            description = "Creates a new support program with details like name, schedule, participants, and category, no sessions attached yet," +
                    "Role: MANAGER, COUNSELOR"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Support program created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SupportProgramResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    @PostMapping
    ResponseEntity<ApiResponse<SupportProgramResponse>> createSupportProgram(@Valid @RequestBody SupportProgramRequest request) {

        return ResponseEntity.ok(ApiResponse.<SupportProgramResponse>builder()
                .data(service.createSupportProgram(request))
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Support program created successfully")
                .build());
    }

    @Operation(
            summary = "Get support program by ID",
            description = "Retrieves the details of a specific support program by its ID, " +
                    "Role: ANY USERS"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Support program retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SupportProgramResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Support program not found", content = @Content)
    })
    @GetMapping("/{supportProgramId}")
    ResponseEntity<ApiResponse<SupportProgramResponse>> getSupportProgramById(@PathVariable Integer supportProgramId) {

        return ResponseEntity.ok(ApiResponse.<SupportProgramResponse>builder()
                .data(service.getSupportProgramById(supportProgramId))
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Retrieved support program successfully")
                .build());
    }

    @Operation(
            summary = "Get all support programs",
            description = "Retrieves a list of all available support programs"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Support programs retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SupportProgramResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping
    ResponseEntity<ApiResponse<List<SupportProgramResponse>>> getAllSupportPrograms() {

        return ResponseEntity.ok(ApiResponse.<List<SupportProgramResponse>>builder()
                .data(service.getAllSupportPrograms())
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Retrieved support programs successfully")
                .build());
    }

    @Operation(
            summary = "Delete a support program",
            description = "Deletes an existing support program by its ID. " +
                    "Role: MANAGER"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Support program deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Support program not found")
    })
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSupportProgram(@PathVariable Integer id) {

        service.deleteSupportProgram(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update a support program",
            description = "Updates an existing support program with the provided data. " +
                    "Role: COUNSELOR, MANAGER"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Support program updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Support program or category not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<SupportProgramResponse>> updateSupportProgram(@PathVariable Integer id,
                                                                             @RequestBody SupportProgramRequest request) {

        return ResponseEntity.ok(ApiResponse.<SupportProgramResponse>builder()
                .data(service.updateSupportProgram(id, request))
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Update support program successfully")
                .build());
    }
}
