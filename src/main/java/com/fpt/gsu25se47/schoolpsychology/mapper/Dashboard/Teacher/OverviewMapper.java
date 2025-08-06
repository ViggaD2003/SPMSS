package com.fpt.gsu25se47.schoolpsychology.mapper.Dashboard.Teacher;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.OverviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OverviewMapper {

    @Mapping(target = "totalStudents", source = "totalStudents")
    @Mapping(target = "studentsWithCases", source = "studentsWithCases")
    @Mapping(target = "studentsInPrograms", source = "studentsInPrograms")
    @Mapping(target = "studentsCompletedSurveys", source = "studentsCompletedSurveys")
    OverviewResponse toOverviewResponse(Integer totalStudents, Integer studentsWithCases,
                                        Integer studentsInPrograms, Integer studentsCompletedSurveys);
}
