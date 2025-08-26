package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewSurveyDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyStatic;
import com.fpt.gsu25se47.schoolpsychology.model.Category;
import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import com.fpt.gsu25se47.schoolpsychology.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SurveyMapper {

    private final QuestionMapper questionMapper;
    private final CategoryMapper categoryMapper;
    private final GradeMapper gradeMapper;
    private final AccountMapper accountMapper;

    /**
     * Ánh xạ AddNewSurveyDto thành Survey entity.
     */
    public Survey mapToSurvey(AddNewSurveyDto dto, CategoryRepository categoryRepository) {
        validateAddNewSurveyDto(dto);

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return Survey.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .isRequired(dto.getIsRequired())
                .isRecurring(dto.getIsRecurring())
                .recurringCycle(dto.getRecurringCycle())
                .surveyType(dto.getSurveyType())
                .targetScope(dto.getTargetScope())
                .targetGradeLevel(dto.getTargetGrade())
                .category(category)
                .questions(dto.getQuestions().stream()
                        .map(questionMapper::mapToQuestion)
                        .toList())
                .build();
    }

    /**
     * Ánh xạ Survey entity sang SurveyGetAllResponse DTO.
     */
    public SurveyGetAllResponse mapToSurveyGetAllResponse(Survey survey) {
        return SurveyGetAllResponse.builder()
                .surveyId(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .startDate(survey.getStartDate())
                .endDate(survey.getEndDate())
                .status(survey.getStatus().name())
                .isRecurring(survey.getIsRecurring())
                .isRequired(survey.getIsRequired())
                .recurringCycle(survey.getRecurringCycle().name())
                .targetGrade(survey.getTargetGradeLevel() == null ? null : survey.getTargetGradeLevel().stream().map(gradeMapper::from).toList())
                .targetScope(survey.getTargetScope().name())
                .surveyType(survey.getSurveyType().name())
                .createdAt(survey.getCreatedDate())
                .updatedAt(survey.getUpdatedDate())
                .category(categoryMapper.mapToCategorySurveyResponse(survey.getCategory()))
                .createdBy(null)
                .build();
    }

    /**
     * Ánh xạ Survey entity sang SurveyDetailResponse DTO.
     */
    public SurveyDetailResponse mapToSurveyDetailResponse(Survey survey) {
        return SurveyDetailResponse.builder()
                .surveyId(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .startDate(survey.getStartDate())
                .endDate(survey.getEndDate())
                .status(survey.getStatus().name())
                .isRecurring(survey.getIsRecurring())
                .isRequired(survey.getIsRequired())
                .recurringCycle(survey.getRecurringCycle().name())
                .targetGrade(survey.getTargetGradeLevel() == null ? null : survey.getTargetGradeLevel().stream().map(gradeMapper::from).toList())
                .targetScope(survey.getTargetScope().name())
                .surveyType(survey.getSurveyType().name())
                .createdAt(survey.getCreatedDate())
                .updatedAt(survey.getUpdatedDate())
                .category(categoryMapper.mapToCategorySurveyResponse(survey.getCategory()))
                .questions(survey.getQuestions().stream()
                        .filter(q -> Boolean.TRUE.equals(q.getIsActive()))
                        .map(questionMapper::mapToQuestionResponse)
                        .toList())
                .build();
    }

    /**
     * Validate dữ liệu đầu vào khi tạo survey.
     */
    private void validateAddNewSurveyDto(AddNewSurveyDto dto) {
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new IllegalArgumentException("Start and end date cannot be null");
        }

        if (!dto.getEndDate().isAfter(dto.getStartDate())) {
            throw new RuntimeException("End date must be after start date");
        }

        if (dto.getIsRequired() && dto.getRecurringCycle() == null) {
            throw new RuntimeException("Recurring cycle is required when survey is required");
        }

        if (dto.getCategoryId() == null) {
            throw new RuntimeException("Category ID cannot be null");
        }
    }
}
