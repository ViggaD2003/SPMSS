package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramSessionStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateProgramSessionRequest {

    private Integer slotId;

    private Integer supportProgramId;

    private Integer hostById;

    private String topic;

    private String description;

    private ProgramSessionStatus status;

    private LocalDate date;
}
