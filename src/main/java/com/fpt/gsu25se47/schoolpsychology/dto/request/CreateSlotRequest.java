package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Data
public class CreateSlotRequest {

    @NotBlank(message = "Slot name must not be blank")
    @JsonSetter(nulls = Nulls.SKIP)
    private String slotName = "Slot " + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));

    @NotNull(message = "Start date and time must not be null")
    @FutureOrPresent(message = "Ngày bắt đầu phải là hôm nay hoặc sau đó")
    private LocalDateTime startDateTime;

    @NotNull(message = "End date and time must not be null")
    @Future(message = "End date and time must be in the future")
    private LocalDateTime endDateTime;

    @NotBlank(message = "Slot type must not be blank")
    @JsonSetter(nulls = Nulls.SKIP)
    private SlotStatus status = SlotStatus.PUBLISHED;

    private Integer hostById;
}
