package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.response.EventResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/{studentId}")
    ResponseEntity<List<EventResponse>> getTeacherDashboard(@PathVariable Integer studentId,
                                                            @RequestParam LocalDate startDate,
                                                            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(eventService.getEvents(studentId, startDate, endDate));
    }
}
