package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupportProgramResponse {

    private Integer id;

    private String name;

    private String description;

    private Integer maxParticipants;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean isOnline;

    private ProgramStatus status;

    private String linkMeet;

    private CategoryResponse category;

    private List<Integer> programRegistrations;

    private SurveyGetAllResponse programSurvey;
}
