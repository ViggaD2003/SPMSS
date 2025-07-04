package com.fpt.gsu25se47.schoolpsychology.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmAppointment {

    @NotNull(message = "Appointment can not be null")
    private Integer appointmentId;

    @NotNull(message = "Location for appointment can not be null")
    private String location;
}
