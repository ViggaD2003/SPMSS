package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard;

import com.fpt.gsu25se47.schoolpsychology.dto.response.CounselorDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentDto;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Priority;
import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgressTrend;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CaseSummDetailResponse {

    private Integer caseId;
    private StudentDto studentDto;
    private String categoryName;
    private Status status;
    private ProgressTrend progressTrend;
    private Priority priority;
    private String initialLevel;
    private String currentLevel;
    private CounselorDto counselorDto;
    private LocalDate lastUpdated;
}
