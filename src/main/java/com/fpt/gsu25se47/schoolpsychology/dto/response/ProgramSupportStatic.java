package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProgramSupportStatic {

    private int activePrograms;

    private int completedPrograms;

    private int numOfAbsent;

    private List<DataSet> dataSet;
}
