package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Levels;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SurveyLevelByCategory {
    private String category;

    private List<Levels> levels;
}
