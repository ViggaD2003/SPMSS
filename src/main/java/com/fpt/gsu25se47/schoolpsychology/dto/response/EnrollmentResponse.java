package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Classes.ClassResponseSRC;
import lombok.Data;

@Data
public class EnrollmentResponse {

    private StudentDto student;
    private ClassResponseSRC classes;
}
