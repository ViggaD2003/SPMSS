package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramSessionStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProgramSessionResponse {

    private Integer id;

    private Integer supportProgramId;

    private SlotResponse slot;

    private AccountDto hostBy;

    private String topic;

    private String description;

    private ProgramSessionStatus status;

    private LocalDate date;
}
