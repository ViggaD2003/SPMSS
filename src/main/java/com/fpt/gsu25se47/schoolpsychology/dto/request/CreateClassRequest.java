package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateClassRequest {

    @NotNull(message = "Grade must not be null")
    private Grade grade;

    @NotNull(message = "Teacher ID is required")
    private Integer teacherId;

    @NotBlank(message = "Class code must not be blank")
    private String codeClass;

    private Integer schoolYearId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private boolean isActive = true;

}
