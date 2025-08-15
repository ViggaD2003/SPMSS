package com.fpt.gsu25se47.schoolpsychology.dto.response.Classes;

import com.fpt.gsu25se47.schoolpsychology.dto.response.TeacherDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassDto {

    private TeacherDto teacher;

    private String codeClass;

    private String schoolYear;
}
