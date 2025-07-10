package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateSupportProgramRequest {

    private String name;

    private String description;

    private Integer maxParticipants;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isOnline;

    private ProgramStatus status;

    private String location;

    private Integer categoryId;
}
