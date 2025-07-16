package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.LevelName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddNewLevelDto {

    private String label;

    private Integer minScore;

    private Integer maxScore;

    private LevelName levelName;
}
