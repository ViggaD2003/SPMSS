package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassDto;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TeacherMapper.class)

public interface ClassMapper {

    @BeanMapping(builder = @Builder(disableBuilder = true))
    ClassDto toDto(Classes classes);
}
