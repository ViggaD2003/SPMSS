package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateClassRequest {

    @NotNull
    private Grade grade;

    @NotNull
    private Integer teacherId;

    @NotBlank
    private String codeClass;

    @NotBlank
    private String schoolYear;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @NotNull
    private Boolean isActive;
}
