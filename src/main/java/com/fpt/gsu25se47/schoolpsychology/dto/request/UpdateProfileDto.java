package com.fpt.gsu25se47.schoolpsychology.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UpdateProfileDto {

    private String fullName;

    private String phoneNumber;

    private Boolean gender;

    private LocalDate dob;

    private Boolean isEnableSurvey;
}
