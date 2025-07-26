package com.fpt.gsu25se47.schoolpsychology.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateSlotRequest {

    @NotNull(message = "Start date and time must not be null")
    @FutureOrPresent(message = "Ngày bắt đầu phải là hôm nay hoặc sau đó")
    private LocalDateTime startDateTime;

    @NotNull(message = "End date and time must not be null")
    @Future(message = "End date and time must be in the future")
    private LocalDateTime endDateTime;
}
