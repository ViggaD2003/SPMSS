package com.fpt.gsu25se47.schoolpsychology.mapper.Dashboard.Teacher;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.AlertSkippedSurveyResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.StudentSkippedSurveyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlertSkippedSurveyMapper {

    @Mapping(target = "totalSkippedThisMonth", source = "totalSkippedThisMonth")
    @Mapping(target = "students", source = "studentSkippedSurveyResponses")
    AlertSkippedSurveyResponse toAlertSkippedSurveyResponse(List<StudentSkippedSurveyResponse> studentSkippedSurveyResponses,
                                                            Integer totalSkippedThisMonth);
}
