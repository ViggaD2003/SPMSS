package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SurveyStatic {

    private int totalSurvey;

    private int numberOfSkips;

    private List<DataSet> dataSet;
}
