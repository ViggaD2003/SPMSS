package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.CategoryResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse mapToCategorySurveyResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .code(category.getCode())
                .name(category.getName())
                .isSum(category.getIsSum())
                .description(category.getDescription())
                .maxScore(category.getMaxScore())
                .minScore(category.getMinScore())
                .questionLength(category.getQuestionLength())
                .isActive(category.getIsActive())
                .severityWeight(category.getSeverityWeight())
                .isLimited(category.getIsLimited())
                .build();
    }
}
