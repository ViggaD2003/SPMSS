package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateClassRequest {

    @NotNull(message = "Grade must not be null")
    private Grade grade;

    @NotNull(message = "Teacher ID is required")
    private Integer teacherId;

    @NotBlank(message = "Class code must not be blank")
    private String codeClass;

    @NotBlank(message = "School year must not be blank")
    @Pattern(
            regexp = "^[0-9]{4}-[0-9]{4}$",
            message = "School year must follow the format 'YYYY-YYYY'"
    )
    private String schoolYear;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    // Optional, but initialized to true by default
    private Boolean isActive = true;
}
