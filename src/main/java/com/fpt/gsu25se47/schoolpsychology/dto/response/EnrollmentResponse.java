package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Classes.ClassResponseSRC;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentDto;
import lombok.Data;

@Data
public class EnrollmentResponse {

    private StudentDto student;
    private ClassResponseSRC classes;
}
