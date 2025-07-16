package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.RegistrationStatus;
import lombok.Data;

@Data
public class ProgramRegistrationRequest {

    private Integer supportProgramId;

    private Integer accountId;

    private RegistrationStatus status;
}
