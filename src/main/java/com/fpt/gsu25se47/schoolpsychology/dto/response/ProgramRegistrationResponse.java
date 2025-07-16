package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.model.enums.RegistrationStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProgramRegistrationResponse {

    private Integer id;

    private Integer supportProgramId;

    private String supportProgramName;

    private LocalDate programStartDate;

    private LocalDate programEndDate;

    private String location;

    private Boolean isOnline;

    private AccountDto account;

    private RegistrationStatus status;

    private LocalDate registeredAt;
}
