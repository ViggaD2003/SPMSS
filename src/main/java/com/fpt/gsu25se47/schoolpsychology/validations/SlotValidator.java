package com.fpt.gsu25se47.schoolpsychology.validations;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SlotValidator {

    public static void validateSlotWithinSession(LocalDateTime start, LocalDateTime end, LocalDate sessionDate) {

        if (start.isEqual(end)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Start and end time must not be the same"
            );
        }

        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();

        if (!startDate.equals(sessionDate) || !endDate.equals(sessionDate)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Slot datetime must match session date: " + sessionDate);
        }
    }
}
