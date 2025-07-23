package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Priority;
import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgressTrend;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
public class AddNewCaseDto {

    @NotBlank(message = "Title must not be blank")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Priority must not be null")
    private Priority priority;

    @NotNull(message = "Progress trend must not be null")
    private ProgressTrend progressTrend;

    @NotNull(message = "Student ID must not be null")
    @Positive(message = "Student ID must be a positive number")
    private Integer studentId;

    @NotNull(message = "Created by (user ID) must not be null")
    @Positive(message = "Created by must be a positive number")
    private Integer createBy;

    @NotNull(message = "Counselor ID must not be null")
    @Positive(message = "Counselor ID must be a positive number")
    private Integer counselorId;

    @NotNull(message = "Current level ID must not be null")
    @Positive(message = "Current level ID must be a positive number")
    private Integer currentLevelId;

    @NotNull(message = "Initial level ID must not be null")
    @Positive(message = "Initial level ID must be a positive number")
    private Integer initialLevelId;
}
