package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.StudentDto;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentMapper {

    private final ClassMapper classMapper;

    public StudentDto mapToStudentDto(Student student) {
        return StudentDto.builder()
                .studentCode(student.getStudentCode())
                .isEnableSurvey(student.getIsEnableSurvey())
                .classDto(student.getClasses() != null ? classMapper.mapToClassDto(student.getClasses()) : null)
                .build();
    }
}
