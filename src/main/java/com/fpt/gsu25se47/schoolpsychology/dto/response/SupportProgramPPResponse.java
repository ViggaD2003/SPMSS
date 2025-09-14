package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.RegistrationStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class SupportProgramPPResponse {

    private Integer id;

    private String name;

    private String description;

    private Integer maxParticipants;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private ProgramStatus status;

    private Map<String, String> thumbnail;

    private String location;

    private CategoryResponse category;

    private AccountDto hostedBy;

    private SurveyGetAllResponse programSurvey;

    private Integer participants;

    private RegistrationStatus registrationStatus;
}
