package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.TeacherOfClassDto;
import com.fpt.gsu25se47.schoolpsychology.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeacherOfClassMapper {

    @Mapping(target = "phoneNumber", source = "teacher.account.phoneNumber")
    @Mapping(target = "fullName", source = "teacher.account.fullName")
    @Mapping(target = "email", source = "teacher.account.email")
    TeacherOfClassDto mapToTeacherOfClassDto(Teacher teacher);
}
