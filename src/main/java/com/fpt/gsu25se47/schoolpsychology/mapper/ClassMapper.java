package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TeacherMapper.class})
public interface ClassMapper {

    Classes toClassEntity(CreateClassRequest request);

    @Mapping(target = "students", ignore = true)
    ClassResponse toClassResponse(Classes classes);
}
