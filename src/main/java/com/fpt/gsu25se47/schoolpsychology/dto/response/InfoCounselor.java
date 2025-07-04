package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class InfoCounselor {
    private Integer id;

    private String counselorCode;

    private String phoneNumber;

    private String fullName;

    private Boolean gender;

    private LocalDate dob;
}
