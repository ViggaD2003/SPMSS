package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;
import com.fpt.gsu25se47.schoolpsychology.model.enums.RecurringCycle;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyType;
import com.fpt.gsu25se47.schoolpsychology.model.enums.TargetScope;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class AddNewSurveyDto {

    @NotBlank(message = "Tiêu đề khảo sát không được để trống")
    private String title;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotNull(message = "Loại khảo sát không được để trống")
    private SurveyType surveyType;

    @NotNull(message = "Vui lòng xác định khảo sát có bắt buộc không")
    private Boolean isRequired;

    @NotNull(message = "Vui lòng xác định khảo sát có lặp lại không")
    private Boolean isRecurring;

    @NotNull(message = "Recurring cycle is required")
    private RecurringCycle recurringCycle;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @FutureOrPresent(message = "Ngày bắt đầu phải là hôm nay hoặc sau đó")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @Future(message = "Ngày kết thúc phải sau ngày hiện tại")
    private LocalDate endDate;

    @NotNull(message = "Danh mục không được để trống")
    private Integer categoryId;

    @NotNull(message = "Phạm vi khảo sát không được để trống")
    private TargetScope targetScope;

    private List<Grade> targetGrade;

    @NotEmpty(message = "Danh sách câu hỏi không được để trống")
    @Valid
    private List<AddNewQuestionDto> questions;
}
