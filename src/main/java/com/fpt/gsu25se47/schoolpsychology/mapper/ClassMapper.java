package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TeacherMapper.class})
public interface ClassMapper {

    Classes toClassEntity(CreateClassRequest request);

    @Mapping(target = "totalStudents", expression = "java(classes.getEnrollments().size())")
    ClassResponse toClassResponse(Classes classes);

    @BeanMapping(builder = @Builder(disableBuilder = true))
    ClassResponseSRC toClassDetailResponseSRC(Classes classes, @Context List<StudentSRCResponse> students);

//    @BeanMapping(builder = @Builder(disableBuilder = true))
//    ClassResponse toClassDetailResponse(Classes classes, @Context List<StudentDto> studentDtos);

    @BeanMapping(builder = @Builder(disableBuilder = true))
    ClassDto toDto(Classes classes);

    @AfterMapping
    default void setStudentsToClassDetailResponseSRC(@MappingTarget ClassResponseSRC classResponseSRC,
                                                  @Context List<StudentSRCResponse> students) {
        classResponseSRC.setStudents(students);
    }
}
