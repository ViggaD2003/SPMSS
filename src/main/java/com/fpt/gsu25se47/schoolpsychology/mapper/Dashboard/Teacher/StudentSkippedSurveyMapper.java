package com.fpt.gsu25se47.schoolpsychology.mapper.Dashboard.Teacher;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.StudentSkippedSurveyResponse;
import com.fpt.gsu25se47.schoolpsychology.model.SurveyRecord;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentSkippedSurveyMapper {

    @Mapping(target = "name", source = "student.fullName")
    StudentSkippedSurveyResponse toStudentSkippedSurveyResponse(SurveyRecord surveyRecord, @Context List<String> skippedSurveys);

    @AfterMapping
    default void setSkippedSurveysToSSSR(@MappingTarget StudentSkippedSurveyResponse response,
                                         @Context List<String> skippedSurveys) {
        response.setSkippedSurveys(skippedSurveys);
    }
}
