package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.SubTypeRequest;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SubTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sub-type")
@RequiredArgsConstructor
public class SubTypeController {

    private final SubTypeService subTypeService;

    @GetMapping
    public ResponseEntity<?> getAllSubTypes() {
        return ResponseEntity.ok(subTypeService.getAllSubTypes());
    }

    @PostMapping
    public ResponseEntity<?> addNewSubType(@Valid @RequestBody SubTypeRequest request) {
     return ResponseEntity.ok(subTypeService.addNewSubType(request));
    }
}
