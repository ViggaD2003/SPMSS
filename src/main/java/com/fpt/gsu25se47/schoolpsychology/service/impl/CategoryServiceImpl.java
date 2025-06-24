package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddCategoryDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.CategoryResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.CategoryMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Category;
import com.fpt.gsu25se47.schoolpsychology.repository.CategoryRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public Optional<CategoryResponse> createCategory(AddCategoryDto addCategoryDto) {
        try {
            if (categoryRepository.findByName(addCategoryDto.getName()).isPresent() ||
                    categoryRepository.findByCode(addCategoryDto.getCode()).isPresent()) {
                log.warn("This category existed in the system with name: {}\n\t code: {}",
                        addCategoryDto.getName(), addCategoryDto.getCode());
                log.warn("Return empty category here");
                return Optional.empty();
            }

            Category categorySaved = categoryRepository.save(categoryMapper.toEntity(addCategoryDto));
            return Optional.of(categoryMapper.toResponse(categorySaved));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Could not create category due to exception");
        }
    }

    @Override
    public List<CategoryResponse> findAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }
}
