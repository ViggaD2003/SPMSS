package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddCategoryDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateCategoryDto;
import com.fpt.gsu25se47.schoolpsychology.service.inter.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody AddCategoryDto addCategoryDto) {
        return ResponseEntity.ok(categoryService.createCategory(addCategoryDto));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('COUNSELOR') or hasRole('STUDENT') or hasRole('TEACHER')")
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
       return ResponseEntity.ok(categoryService.findAllCategories().reversed());
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('COUNSELOR') or hasRole('STUDENT') or hasRole('TEACHER')")
    @GetMapping("/level")
    public ResponseEntity<?> getLevelByCategoryId(@RequestParam(name = "categoryId") Integer categoryId) {
        return ResponseEntity.ok(categoryService.findAllLevelByCategoryId(categoryId).reversed());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatusCategory(@RequestParam("id") Integer categoryId, @RequestParam(name = "status") Boolean status) {
        return ResponseEntity.ok(categoryService.updateStatus(categoryId, status));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer categoryId, @RequestBody UpdateCategoryDto updateCategoryDto) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryId, updateCategoryDto));
    }
}
