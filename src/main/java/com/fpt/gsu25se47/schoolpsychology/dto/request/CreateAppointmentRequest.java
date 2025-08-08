package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.HostType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateAppointmentRequest {

    @NotNull(message = "Slot ID is required")
    private Integer slotId;

    @NotNull(message = "Booked-for ID is required")
    private Integer bookedForId;

    @NotNull(message = "Appointment type (online/offline) must be specified")
    private Boolean isOnline;

    @NotNull(message = "Start date/time is required")
    @FutureOrPresent(message = "Start date/time must be in the present or future")
    private LocalDateTime startDateTime;

    @NotNull(message = "End date/time is required")
    @Future(message = "End date/time must be in the future")
    private LocalDateTime endDateTime;

    private HostType hostType;

    @NotBlank(message = "Reason for booking is required")
    private String reasonBooking;
}
