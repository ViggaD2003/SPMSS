package com.fpt.gsu25se47.schoolpsychology.mapper.Dashboard.Teacher;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.CaseSummDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CaseSummDetailMapper {

    @Mapping(target = "studentName", source = "student.fullName")
    @Mapping(target = "level", source = "currentLevel.label")
    @Mapping(target = "lastUpdated", source = "updatedDate")
    @Mapping(target = "counselorName", source = "counselor.fullName")
    @Mapping(target = "categoryName", source = "currentLevel.category.name")
    CaseSummDetailResponse toCaseSummDetailResponse(Cases cases);
}
