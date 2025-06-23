package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddCategoryDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.CategoryResponse;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Optional<CategoryResponse> createCategory(AddCategoryDto addCategoryDto);
    List<CategoryResponse> findAllCategories();
}
