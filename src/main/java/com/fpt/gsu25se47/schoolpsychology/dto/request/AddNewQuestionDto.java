package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.ModuleType;
import com.fpt.gsu25se47.schoolpsychology.model.enums.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddNewQuestionDto {

    @NotBlank(message = "Nội dung câu hỏi không được để trống")
    private String text;

    @Size(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    private String description;

    @NotNull(message = "Loại câu hỏi không được để trống")
    private QuestionType questionType;

    @NotNull(message = "Loại module không được để trống")
    private ModuleType moduleType;

    @NotNull(message = "Require không được để trống")
    private boolean isRequired;

    @NotNull(message = "Category ID không được để trống")
    private Integer categoryId;

    @NotNull(message = "Danh sách câu trả lời không được để trống")
    @Size(min = 1, message = "Phải có ít nhất một câu trả lời")
    @Valid
    private List<AddNewAnswerDto> answers;
}
