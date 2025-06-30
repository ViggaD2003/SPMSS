package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.common.ApiResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddCategoryDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.CategoryResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCategory(@RequestBody AddCategoryDto addCategoryDto) {

        Optional<CategoryResponse> categoryResponse = categoryService.createCategory(addCategoryDto);

        if (categoryResponse.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(
                            ApiResponse.<Void>builder()
                                    .statusCode(HttpStatus.OK.value())
                                    .success(false)
                                    .message("Category name or code already exists.")
                                    .data(null)
                                    .build()
                    );
        }
        return ResponseEntity
                .ok(ApiResponse.<CategoryResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .success(true)
                        .message("Category created successfully")
                        .data(categoryResponse.get())
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllCategories() {
        var categories = categoryService.findAllCategories();
        if (categories.isEmpty()) {
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .statusCode(HttpStatus.OK.value())
                            .success(true)
                            .message("No categories yet")
                            .data(categories)
                            .build());
        }
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .success(true)
                        .message("Categories retrieved successfully")
                        .data(categories)
                        .build()
        );
    }
}
