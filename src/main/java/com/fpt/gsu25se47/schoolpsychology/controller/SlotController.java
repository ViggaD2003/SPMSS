package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/slot")
@RequiredArgsConstructor
@Validated
public class SlotController {

    private final SlotService slotService;

    @PreAuthorize("hasRole('TEACHER') or hasRole('COUNSELOR')")
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody List<AddSlotRequest> request) {
        return slotService.initSlot(request);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{slotId}")
    public ResponseEntity<?> update(@PathVariable Integer slotId, @Valid @RequestBody UpdateSlotRequest request) {
        return ResponseEntity.ok(slotService.updateSlot(slotId, request));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/{slotId}/status")
    public ResponseEntity<?> updateSlotStatus(@PathVariable Integer slotId) {
        return ResponseEntity.ok(slotService.updateStatusSlot(slotId));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER') or hasRole('COUNSELOR') or hasRole('STUDENT') or hasRole('PARENTS')")
    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(required = false) Integer hostById) {
        return ResponseEntity.ok(slotService.getAllSlotsByHostBy(hostById));
    }

}
