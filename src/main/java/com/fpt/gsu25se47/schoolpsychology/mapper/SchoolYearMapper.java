package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.SchoolYear.SchoolYearResponse;
import com.fpt.gsu25se47.schoolpsychology.model.SchoolYear;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SchoolYearMapper {

    SchoolYearResponse toSchoolYearResponse(SchoolYear schoolYear);

}
