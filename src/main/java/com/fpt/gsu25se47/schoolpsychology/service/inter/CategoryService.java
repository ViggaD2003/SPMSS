package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddCategoryDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateCategoryDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.CategoryResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.LevelResponse;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Optional<?> createCategory(AddCategoryDto addCategoryDto);
    List<CategoryResponse> findAllCategories();
    List<LevelResponse> findAllLevelByCategoryId(Integer categoryId);
    List<LevelResponse> findAllLevelByCategoryName(String name);
    CategoryResponse updateCategory(Integer categoryId, UpdateCategoryDto updateCategoryDto);
    CategoryResponse updateStatus(Integer categoryId, Boolean status);
}
