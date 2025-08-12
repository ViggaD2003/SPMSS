package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.model.enums.RegistrationStatus;
import lombok.Data;

@Data
public class ProgramPPParticipantResponse {

    private Integer studentId;
    private RegistrationStatus registrationStatus;
}
