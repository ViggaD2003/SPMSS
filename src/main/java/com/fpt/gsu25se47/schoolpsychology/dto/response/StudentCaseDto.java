package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class StudentCaseDto{
    private Integer id;
    private String studentCode;
    private String fullName;
    private LocalDate dob;
    private Boolean gender;
//    private String className;
}
