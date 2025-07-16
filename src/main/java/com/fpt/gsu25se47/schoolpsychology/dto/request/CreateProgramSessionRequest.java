package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramSessionStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateProgramSessionRequest {

    private Integer supportProgramId;

    private String topic;

    private String description;

    private ProgramSessionStatus status;

    private LocalDate date;

    private CreateSlotRequest createSlotRequest;
}
