package com.fpt.gsu25se47.schoolpsychology.dto.response.Cases;

import com.fpt.gsu25se47.schoolpsychology.dto.response.AccountDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.LevelResponse;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Priority;
import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgressTrend;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CaseGetAllResponse {
    private Integer id;

    private Integer categoryId;

    private String categoryName;

    private String codeCategory;

    private String title;

    private Boolean isAddSurvey;

    private String description;

    private Priority priority;

    private Status status;

    private ProgressTrend progressTrend;

    private AccountDto student;

    private AccountDto createBy;

    private AccountDto counselor;

    private LevelResponse currentLevel;

    private LevelResponse initialLevel;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean notify;
}

