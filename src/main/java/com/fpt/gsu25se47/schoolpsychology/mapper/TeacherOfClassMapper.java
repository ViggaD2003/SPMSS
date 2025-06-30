package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.TeacherOfClassDto;
import com.fpt.gsu25se47.schoolpsychology.model.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherOfClassMapper {

    public TeacherOfClassDto mapToTeacherOfClassDto(Teacher teacher) {

        return TeacherOfClassDto.builder()
                .email(teacher.getAccount().getEmail())
                .teacherCode(teacher.getTeacherCode())
                .fullName(teacher.getAccount().getFullName())
                .phoneNumber(teacher.getAccount().getPhoneNumber())
                .build();
    }
}
