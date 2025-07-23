package com.fpt.gsu25se47.schoolpsychology.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Data
@Builder
public class AddCategoryDto {

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Code must not be blank")
    private String code;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "isSum must not be null")
    private Boolean isSum;

    @NotNull(message = "isLimited must not be null")
    private Boolean isLimited;

    @NotNull(message = "Question length must not be null")
    @Min(value = 1, message = "Question length must be at least 1")
    private Integer questionLength;

    @NotNull(message = "Severity weight must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Severity weight must be greater than 0")
    private Float severityWeight;

    @NotNull(message = "Max score must not be null")
    @Min(value = 0, message = "Max score must be at least 0")
    private Integer maxScore;

    @NotNull(message = "Min score must not be null")
    @Min(value = 0, message = "Min score must be at least 0")
    private Integer minScore;

    @Valid
    @NotEmpty(message = "Levels must not be empty")
    private List<AddNewLevelDto> levels;
}
