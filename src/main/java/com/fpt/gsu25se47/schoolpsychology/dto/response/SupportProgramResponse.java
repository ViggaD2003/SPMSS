package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SupportProgramResponse {

    private Integer id;

    private String name;

    private String description;

    private Integer maxParticipants;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private ProgramStatus status;

    private Boolean isActiveSurvey;

    private String thumbnail;

    private String location;

    private CategoryResponse category;

    private AccountDto hostedBy;

    private SurveyGetAllResponse programSurvey;

    private Integer participants;

}
