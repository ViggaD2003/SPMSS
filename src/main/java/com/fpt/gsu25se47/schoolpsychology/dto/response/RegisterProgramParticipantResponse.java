package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.model.enums.RegistrationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterProgramParticipantResponse {

    private Integer id;

    private SupportProgramResponse supportProgram;

    private StudentDto student;

    private LocalDateTime joinAt;

    private RegistrationStatus status;

}
