package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Parent.ParentDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentDto;
import com.fpt.gsu25se47.schoolpsychology.model.Guardian;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ParentMapper {

    @Autowired
    protected StudentMapper studentMapper;

    @Mapping(target = "roleName", source = "account.role")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "dob", source = "account.dob")
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "relationshipType", ignore = true)
    public abstract ParentDto toDto(Guardian parent);

    public ParentDto toParentWithRelationshipDto(Guardian parent) {
        ParentDto dto = toDto(parent);

        List<Student> students = new ArrayList<>();
        parent.getRelationships().forEach(rl -> {
            dto.setRelationshipType(rl.getRelationshipType().name());
            students.add(rl.getStudent());
        });

        List<StudentDto> studentDtos = students.stream().map(s -> studentMapper.mapStudentDtoWithClass(s)).toList();

        dto.setStudent(studentDtos);
        return dto;
    }
}

