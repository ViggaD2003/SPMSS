package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Cases.CaseGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.model.enums.RegistrationStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProgramParticipantsResponse {
    private Integer id;

    private AccountDto student;

    private CaseGetAllResponse cases;

    private LocalDateTime joinAt;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

    private Float finalScore;

    private Float surveyIn;

    private Float surveyOut;
}
