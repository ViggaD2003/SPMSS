package com.fpt.gsu25se47.schoolpsychology.dto.request;

import jakarta.validation.constraints.*;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddNewAnswerDto {

    @Min(value = 0, message = "Điểm phải lớn hơn hoặc bằng 0")
    private int score;

    @NotBlank(message = "Nội dung câu trả lời không được để trống")
    private String text;
}
