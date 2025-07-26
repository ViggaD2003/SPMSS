package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Data;

@Data
public class EnrollmentResponse {

    private StudentDto student;
    private ClassResponse classes;
}
