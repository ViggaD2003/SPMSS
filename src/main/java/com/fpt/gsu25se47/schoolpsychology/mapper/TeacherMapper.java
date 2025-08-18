package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.TeacherDto;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import com.fpt.gsu25se47.schoolpsychology.model.Teacher;
import org.mapstruct.*;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    @Named("toTeacherOfClassDto")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "roleName", source = "account.role")
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "dob", source = "account.dob")
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "id", source = "account.id")
    @Mapping(target = "classId", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    TeacherDto toTeacherOfClassDto(Teacher teacher);

    default TeacherDto toTeacherDto(Teacher teacher) {
        TeacherDto teacherDto = toTeacherOfClassDto(teacher);
        Optional<Classes> classes = teacher.getClasses().stream()
                .filter(Classes::getIsActive)
                .findFirst();

        classes.ifPresent(value -> {
            teacherDto.setClassId(value.getId());
            teacherDto.setIsActive(value.getIsActive());
        });

        return teacherDto;
    }
}
