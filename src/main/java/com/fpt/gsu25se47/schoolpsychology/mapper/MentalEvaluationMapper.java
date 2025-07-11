package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateMentalEvaluationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.MentalEvaluationResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Category;
import com.fpt.gsu25se47.schoolpsychology.model.MentalEvaluation;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StudentMapper.class})
public interface MentalEvaluationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category")
    MentalEvaluation mapToMentalEvaluation(CreateMentalEvaluationRequest request,
                                           Student student,
                                           Category category);

    @Mapping(target = "student", source = "student", qualifiedByName = "mapStudentWithoutEvaluations")
    MentalEvaluationResponse mapToEvaluationResponse(MentalEvaluation mentalEvaluation);
}
