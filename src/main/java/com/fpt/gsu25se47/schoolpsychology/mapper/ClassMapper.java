package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.StudentDto;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TeacherMapper.class})
public interface ClassMapper {

    Classes toClassEntity(CreateClassRequest request);

    @Mapping(target = "students", ignore = true)
    ClassResponse toClassResponse(Classes classes);

    @BeanMapping(builder = @Builder(disableBuilder = true))
    ClassResponse toClassDetailResponse(Classes classes, @Context List<StudentDto> studentDtos);

    @BeanMapping(builder = @Builder(disableBuilder = true))
    ClassDto toDto(Classes classes);

    @AfterMapping
    default void setStudentsToClassDetailResponse(@MappingTarget ClassResponse classResponse,
                                                  @Context List<StudentDto> studentDtos) {

        classResponse.setStudents(studentDtos);
    }
}
