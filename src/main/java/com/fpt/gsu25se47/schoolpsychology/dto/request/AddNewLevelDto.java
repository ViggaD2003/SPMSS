package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.LevelType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
public class AddNewLevelDto {

    @NotBlank(message = "Label must not be blank")
    private String label;

    @NotBlank(message = "Code must not be blank")
    private String code;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Size(max = 1000, message = "Symptoms description must not exceed 1000 characters")
    private String symptomsDescription;

    @Size(max = 1000, message = "Intervention required must not exceed 1000 characters")
    private String interventionRequired;

    @NotNull(message = "Min score must not be null")
    @DecimalMin(value = "0.0", message = "Min score must be at least 0")
    private Float minScore;

    @NotNull(message = "Max score must not be null")
    @DecimalMin(value = "0.0", message = "Max score must be at least 0")
    private Float maxScore;

    @NotNull(message = "Level type must not be null")
    private LevelType levelType;
}
