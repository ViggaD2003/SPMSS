package com.fpt.gsu25se47.schoolpsychology.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AddNewAppointment {

    @NotNull(message = "Slot ID is required")
    @Min(value = 1, message = "Slot ID must be greater than 0")
    private Integer slotId;

    private Integer bookedForId;

    @NotNull(message = "Booked By ID is required")
    @Min(value = 1, message = "Booked By ID must be greater than 0")
    private Integer bookedById;

    @NotNull(message = "location is required")
    private String location;

    @NotNull(message = "Online status is required")
    private Boolean isOnline;

    @NotNull(message = "Start date and time must not be null")
    private LocalDateTime startDateTime;

    @NotNull(message = "End date and time must not be null")
    private LocalDateTime endDateTime;
}
