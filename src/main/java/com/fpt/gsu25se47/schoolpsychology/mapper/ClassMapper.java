package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TeacherMapper.class})
public interface ClassMapper {

    Classes toClassEntity(CreateClassRequest request);

    @Mapping(target = "students", ignore = true)
    ClassResponse toClassResponse(Classes classes);

    @BeanMapping(builder = @Builder(disableBuilder = true))
    ClassDto toDto(Classes classes);
}
