package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.model.enums.LevelType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LevelResponse {

    private Integer id;

    private String label;

    private String code;

    private Float minScore;

    private Float maxScore;

    private LevelType levelType;

    private String description;

    private String symptomsDescription;

    private String interventionRequired;

}
