package com.fpt.gsu25se47.schoolpsychology.dto.response;

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

    private AccountDto student;

    private AccountDto createBy;

    private AccountDto counselor;

    private LevelResponse currentLevel;

    private LevelResponse initialLevel;
}

