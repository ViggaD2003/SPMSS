package com.fpt.gsu25se47.schoolpsychology.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public abstract class AccountDto {

    private String email;

    private String phoneNumber;

    private String fullName;

    private Boolean gender;

    private LocalDate dob;
}
