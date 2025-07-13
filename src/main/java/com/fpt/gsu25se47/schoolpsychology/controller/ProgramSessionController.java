package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.common.ApiResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateProgramSessionRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ProgramSessionResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ProgramSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/v1/program-sessions")
@RequiredArgsConstructor
public class ProgramSessionController {

    private final ProgramSessionService programSessionService;

    @Operation(
            summary = "Create a new program session",
            description = "Creates a new program session for a specific support program. " +
                    "Role: COUNSELOR, MANAGER"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Program session created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProgramSessionResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('MANAGER')")
    @PostMapping
    ResponseEntity<ApiResponse<ProgramSessionResponse>> createProgramSession(@Valid @RequestBody CreateProgramSessionRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<ProgramSessionResponse>builder()
                        .success(true)
                        .statusCode(HttpStatus.OK.value())
                        .message("Program session created successfully")
                        .data(programSessionService.createProgramSession(request))
                        .build()
        );
    }

    @Operation(
            summary = "Get all program sessions by support program ID",
            description = "Returns all program sessions belonging to a specific support program"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Retrieved program sessions successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProgramSessionResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Program not found", content = @Content)
    })
    @GetMapping("/programs/{programId}")
    ResponseEntity<ApiResponse<List<ProgramSessionResponse>>> getAllProgramSessionsBySupportProgramId(@PathVariable Integer programId) {

        return ResponseEntity.ok(
                ApiResponse.<List<ProgramSessionResponse>>builder()
                        .success(true)
                        .statusCode(HttpStatus.OK.value())
                        .message("Retrieved list of program session by program ID successfully")
                        .data(programSessionService.getAllProgramSessionsBySupportProgramId(programId))
                        .build());
    }

    @Operation(
            summary = "Get all program sessions",
            description = "Retrieves all program sessions in the system"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved all sessions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProgramSessionResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping
    ResponseEntity<ApiResponse<List<ProgramSessionResponse>>> getAllProgramSessions() {

        return ResponseEntity.ok(
                ApiResponse.<List<ProgramSessionResponse>>builder()
                        .success(true)
                        .statusCode(HttpStatus.OK.value())
                        .message("Retrieved list of program session successfully")
                        .data(programSessionService.getAllProgramSessions())
                        .build());
    }

    @Operation(
            summary = "Get program session by ID",
            description = "Returns details of a program session given its ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved session",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProgramSessionResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Program session not found", content = @Content)
    })
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<ProgramSessionResponse>> getById(@PathVariable Integer id) {

        return ResponseEntity.ok(
                ApiResponse.<ProgramSessionResponse>builder()
                        .success(true)
                        .statusCode(HttpStatus.OK.value())
                        .message("Retrieved program session successfully")
                        .data(programSessionService.getProgramSessionById(id))
                        .build());
    }

    @Operation(
            summary = "Delete a program session by ID",
            description = "Deletes the program session with the specified ID, " +
                    "Role: MANAGER"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Program session deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Program session not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgramSession(
            @Parameter(description = "ID of the program session to delete", required = true)
            @PathVariable Integer id) {

        programSessionService.deleteProgramSession(id);

        return ResponseEntity.noContent().build();
    }
}
