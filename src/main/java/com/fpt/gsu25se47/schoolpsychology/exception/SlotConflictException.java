package com.fpt.gsu25se47.schoolpsychology.exception;

import com.fpt.gsu25se47.schoolpsychology.dto.response.SlotConflictError;
import lombok.Getter;

import java.util.List;

@Getter
public class SlotConflictException extends RuntimeException {
    private final List<SlotConflictError> conflictErrors;

    public SlotConflictException(List<SlotConflictError> conflictErrors) {
        super("Slot conflict detected");
        this.conflictErrors = conflictErrors;
    }
}
