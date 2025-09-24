package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddCategoryDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewLevelDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateCategoryDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.CategoryResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.LevelResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Category;
import com.fpt.gsu25se47.schoolpsychology.model.Level;
import com.fpt.gsu25se47.schoolpsychology.repository.CategoryRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.LevelRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final LevelRepository levelRepository;

    @Override
    @Transactional
    public Optional<?> createCategory(AddCategoryDto addCategoryDto) {
        Category category = this.mapToCategory(addCategoryDto);

        List<Level> levels = category.getLevels();
        levels.forEach(level -> level.setCategory(category));

        categoryRepository.save(category);
        return Optional.of("Category created");
    }

    @Override
    public List<CategoryResponse> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        List<CategoryResponse> responses = categories.stream()
                .map(this::mapToCategoryGetAllResponse)
                .toList();

        return responses;
    }

    @Override
    public List<LevelResponse> findAllLevelByCategoryId(Integer categoryId) {
        List<Level> levels = levelRepository.findAllByCategoryId(categoryId);

        List<LevelResponse> responses = levels.stream()
                .map(this::mapToLevelResponse)
                .toList();

        return responses;
    }

    @Override
    public List<LevelResponse> findAllLevelByCategoryName(String name) {
        List<Level> levels = levelRepository.findAllByCategoryName(name);

        return levels.stream()
                .map(this::mapToLevelResponse)
                .toList();
    }

    @Override
    public CategoryResponse updateCategory(Integer categoryId, UpdateCategoryDto updateCategoryDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

            category.setName(updateCategoryDto.getName());
            category.setDescription(updateCategoryDto.getDescription());
            category.setCode(updateCategoryDto.getCode());
            category.setIsLimited(updateCategoryDto.getIsLimited());
            category.setMinScore(updateCategoryDto.getMinScore());
            category.setMaxScore(updateCategoryDto.getMaxScore());
            category.setIsSum(updateCategoryDto.getIsSum());
            category.setQuestionLength(updateCategoryDto.getQuestionLength());
            category.setSeverityWeight(updateCategoryDto.getSeverityWeight());
            return this.mapToCategoryGetAllResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse updateStatus(Integer categoryId, Boolean status) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        category.setIsActive(status);
        return this.mapToCategoryGetAllResponse(categoryRepository.save(category));
    }


    private CategoryResponse mapToCategoryGetAllResponse(Category category) {
        return  CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .code(category.getCode())
                .description(category.getDescription())
                .isSum(category.getIsSum())
                .isLimited(category.getIsLimited())
                .questionLength(category.getQuestionLength())
                .severityWeight(category.getSeverityWeight())
                .isActive(category.getIsActive())
                .maxScore(category.getMaxScore())
                .minScore(category.getMinScore())
                .build();
    }

    private LevelResponse mapToLevelResponse(Level level) {
        return LevelResponse.builder()
                .id(level.getId())
                .label(level.getLabel())
                .minScore(level.getMinScore())
                .maxScore(level.getMaxScore())
                .levelType(level.getLevelType())
                .code(level.getCode())
                .description(level.getDescription())
                .symptomsDescription(level.getSymptomsDescription())
                .interventionRequired(level.getInterventionRequired())
                .build();
    }

    private Category mapToCategory(AddCategoryDto dto) {



        return Category.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .description(dto.getDescription())
                .isSum(dto.getIsSum())
                .isLimited(dto.getIsLimited())
                .questionLength(dto.getQuestionLength())
                .severityWeight(dto.getSeverityWeight())
                .isActive(true)
                .maxScore(dto.getMaxScore())
                .minScore(dto.getMinScore())
                .levels(dto.getLevels().stream().map(this::mapToLevel).collect(Collectors.toList()))
                .build();
    }

    private Level mapToLevel(AddNewLevelDto dto) {
        return Level.builder()
                .label(dto.getLabel())
                .minScore(dto.getMinScore())
                .maxScore(dto.getMaxScore())
                .levelType(dto.getLevelType())
                .code(dto.getCode())
                .description(dto.getDescription())
                .symptomsDescription(dto.getSymptomsDescription())
                .interventionRequired(dto.getInterventionRequired())
                .build();
    }

}
