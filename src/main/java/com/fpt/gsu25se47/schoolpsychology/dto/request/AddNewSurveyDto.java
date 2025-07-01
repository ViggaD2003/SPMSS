package com.fpt.gsu25se47.schoolpsychology.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class AddNewSurveyDto {

    @NotBlank(message = "Tên khảo sát không được để trống")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotNull(message = "Vui lòng xác định khảo sát có bắt buộc không")
    private Boolean isRequired;

    @NotNull(message = "Vui lòng xác định khảo sát có lặp lại không")
    private Boolean isRecurring;

    // Nếu isRecurring = true thì recurringCycle phải có giá trị (handled in custom validator or service logic)
    @Size(max = 50, message = "Chu kỳ lặp không được vượt quá 50 ký tự")
    private String recurringCycle;

    @NotBlank(message = "Hãy nhập survey code")
    private String surveyCode;

    @NotNull(message = "Ngày bắt đầu không được để trống")
        @FutureOrPresent(message = "Ngày bắt đầu phải là hôm nay hoặc sau đó")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @Future(message = "Ngày kết thúc phải sau ngày hiện tại")
    private LocalDate endDate;

    @NotEmpty(message = "Danh sách câu hỏi không được để trống")
    @Valid
    private List<AddNewQuestionDto> questions;
}
