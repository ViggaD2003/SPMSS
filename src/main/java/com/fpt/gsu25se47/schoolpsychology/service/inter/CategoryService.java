package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddCategoryDto;

import java.util.Optional;

public interface CategoryService {

    Optional<?> createCategory(AddCategoryDto addCategoryDto);
    Optional<?> findAllCategories();
    Optional<?> findAllLevelByCategoryId(Integer categoryId);
}
