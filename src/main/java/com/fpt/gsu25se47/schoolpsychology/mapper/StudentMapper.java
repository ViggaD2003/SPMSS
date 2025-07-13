package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.StudentDto;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ClassMapper.class, MentalEvaluationMapper.class})
public interface StudentMapper {

    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "dob", source = "account.dob")
    @Mapping(target = "mentalEvaluations", source = "mentalEvaluations")
    @Mapping(target = "classDto", source = "classes")
    StudentDto mapToStudentDto(Student student);

    @Named("mapStudentWithoutEvaluations")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "dob", source = "account.dob")
    @Mapping(target = "mentalEvaluations", ignore = true)
    @Mapping(target = "classDto", source = "classes")
    StudentDto mapStudentWithoutEvaluations(Student student);

}
