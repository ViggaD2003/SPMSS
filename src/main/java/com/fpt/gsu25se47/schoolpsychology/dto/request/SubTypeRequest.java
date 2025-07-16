package com.fpt.gsu25se47.schoolpsychology.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SubTypeRequest {

    @NotBlank(message = "Code name must not be blank")
    @Size(max = 50, message = "Code name must be at most 50 characters")
    private String codeName;

    @NotBlank(message = "Description must not be blank")
    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;

    @NotNull(message = "limitedQuestions must not be null")
    private Boolean limitedQuestions;

    @NotNull(message = "Length must not be null")
    @Min(value = 1, message = "Length must be at least 1")
    private Integer length;

    @NotNull(message = "List of levels must not be null")
    @Size(min = 1, message = "At least one level must be provided")
    private List<@Valid AddNewLevelDto> addNewLevels;
}
