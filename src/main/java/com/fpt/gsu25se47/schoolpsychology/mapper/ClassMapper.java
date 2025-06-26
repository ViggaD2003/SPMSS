package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassDto;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClassMapper {

    private final TeacherOfClassMapper teacherOfClassMapper;

    public ClassDto mapToClassDto(Classes classes) {

        return ClassDto.builder()
                .codeClass(classes.getCodeClass())
                .classYear(classes.getClassYear())
                .teacher(teacherOfClassMapper.mapToTeacherOfClassDto(classes.getTeacher()))
                .build();
    }
}
