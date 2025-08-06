package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.LevelResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Levels;
import com.fpt.gsu25se47.schoolpsychology.model.Level;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LevelMapper {

    public LevelResponse mapToLevelResponse(Level level) {
        return LevelResponse.builder()
                .id(level.getId())
                .label(level.getLabel())
                .minScore(level.getMinScore())
                .maxScore(level.getMaxScore())
                .levelType(level.getLevelType())
                .code(level.getCode())
                .description(level.getDescription())
                .symptomsDescription(level.getSymptomsDescription())
                .interventionRequired(level.getInterventionRequired())
                .build();
    }

    public Levels mapToLevelsResponse(Level levels) {
        return Levels.builder()
                .level(levels.getLevelType().name())
                .count(levels.getSurveyRecords().size())
                .build();
    }
}
