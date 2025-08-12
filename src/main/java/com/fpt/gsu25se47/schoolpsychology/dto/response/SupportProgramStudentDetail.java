package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SupportProgramStudentDetail {
    private Integer id;

    private Boolean isActiveSurvey;

    private Integer surveyId;

    private String name;

    private String description;

    private Integer maxParticipants;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private ProgramStatus status;

    private String thumbnail;

    private String location;

    private CategoryResponse category;

    private AccountDto hostedBy;

    private int participants;

    private SupportProgramStudent student;
}
