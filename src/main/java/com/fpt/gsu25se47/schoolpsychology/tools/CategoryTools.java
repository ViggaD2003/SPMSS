package com.fpt.gsu25se47.schoolpsychology.tools;

import com.fpt.gsu25se47.schoolpsychology.dto.response.CategoryResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.LevelResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryTools {

    private final CategoryService categoryService;

    @Tool(description = "Lấy tất cả các danh mục, dạng bài, thể loại khảo sát tâm lý có trong hệ thống")
    public List<CategoryResponse> findAllCategories() {
        return categoryService.findAllCategories();
    }

    @Tool(description = "Lấy tất cả các cấp độ theo categoryId cụ thể")
    public List<LevelResponse> findAllLevelByCategoryId(Integer categoryId) {
        return categoryService.findAllLevelByCategoryId(categoryId);
    }

    @Tool(description = "Lấy tất cả các cấp độ (level) theo tên danh mục khảo sát cụ thể")
    public List<LevelResponse> findAllLevelByCategoryName(String categoryName) {
        return categoryService.findAllLevelByCategoryName(categoryName);
    }
}
