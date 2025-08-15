package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSchoolYearRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SchoolYear.SchoolYearResponse;
import com.fpt.gsu25se47.schoolpsychology.model.SchoolYear;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SchoolYearMapper {

    SchoolYearResponse toSchoolYearResponse(SchoolYear schoolYear);

    @Mapping(target = "id", ignore = true)
    SchoolYear toSchoolYear(CreateSchoolYearRequest request);
}
