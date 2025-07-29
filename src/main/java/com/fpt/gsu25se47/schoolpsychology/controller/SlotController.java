package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SlotResponse;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotStatus;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SlotService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/slots")
@RequiredArgsConstructor
@Validated
public class SlotController {

    private final SlotService slotService;

    @PreAuthorize("hasRole('MANAGER') or hasRole('COUNSELOR') or hasRole('TEACHER')")
    @PostMapping
    ResponseEntity<List<SlotResponse>> createSlot(@RequestBody @Valid List<CreateSlotRequest> requests) {
        return ResponseEntity.ok(slotService.createSlots(requests));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/{slotId}")
    ResponseEntity<SlotResponse> update(@PathVariable Integer slotId, @Valid @RequestBody UpdateSlotRequest request) {
        return ResponseEntity.ok(slotService.updateSlot(slotId, request));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/{slotId}/status")
    ResponseEntity<SlotResponse> updateSlotStatus(@PathVariable Integer slotId,
                                                  @RequestParam SlotStatus status) {
        return ResponseEntity.ok(slotService.updateStatusSlot(slotId, status));
    }

    @Operation(summary = "Get all slots by hostById",
    description = """
            Role: STUDENTS, PARENTS as loggedIn account, pass hostById as counselorId, get published slots by hostById
            
            Role: COUNSELOR, TEACHER as loggedIn account, pass hostById same as loggedIn account ID, get all slots of this account
            
            Role: MANAGER as loggedIn account, pass hostById same as loggedIn account ID, get all slots
            """)
    @GetMapping
    ResponseEntity<List<SlotResponse>> findAll(@RequestParam(required = false) Integer hostById) {
        return ResponseEntity.ok(slotService.getAllSlotsByHostBy(hostById));
    }
}
