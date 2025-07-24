package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.model.Level;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Priority;
import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgressTrend;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CaseGetAllResponse {
    private Integer id;

    private String title;

    private String description;

    private Priority priority;

    private Status status;

    private ProgressTrend progressTrend;

    private StudentCaseDto student;

    private InfoAccount createBy;

    private InfoAccount counselor;

    private Level currentLevel;

    private Level initialLevel;
}

