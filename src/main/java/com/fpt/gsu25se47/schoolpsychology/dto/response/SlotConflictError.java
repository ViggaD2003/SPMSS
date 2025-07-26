package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SlotConflictError {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String reason;
}

