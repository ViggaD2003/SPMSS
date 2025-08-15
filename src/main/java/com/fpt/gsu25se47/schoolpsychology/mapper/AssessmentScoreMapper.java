package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAssessmentScoreRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Appointment.AssessmentScoreResponse;
import com.fpt.gsu25se47.schoolpsychology.model.AssessmentScores;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssessmentScoreMapper {

    AssessmentScores toAssessmentScore(CreateAssessmentScoreRequest request);


    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "categoryCode", source = "category.code")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "appointmentId", source = "appointment.id")
    AssessmentScoreResponse toAssessmentScoreResponse(AssessmentScores assessmentScores);
}
