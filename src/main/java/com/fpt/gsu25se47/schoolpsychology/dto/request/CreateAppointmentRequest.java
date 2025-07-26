package com.fpt.gsu25se47.schoolpsychology.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateAppointmentRequest {

    private Integer slotId;

    private Integer bookedForId;

    private Boolean isOnline;

    @FutureOrPresent
    private LocalDateTime startDateTime;

    @Future
    private LocalDateTime endDateTime;

    private String reasonBooking;

}
