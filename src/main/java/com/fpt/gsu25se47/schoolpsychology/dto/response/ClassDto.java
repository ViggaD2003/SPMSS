package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassDto {

    private TeacherOfClassDto teacher;

    private String codeClass;

    private LocalDate classYear;
}
