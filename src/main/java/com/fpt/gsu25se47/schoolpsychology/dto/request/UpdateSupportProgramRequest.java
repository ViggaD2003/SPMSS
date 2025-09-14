package com.fpt.gsu25se47.schoolpsychology.dto.request;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@Data
@Builder
public class UpdateSupportProgramRequest {

    @NotBlank(message = "Tên chương trình không được để trống")
    @Size(max = 255, message = "Tên chương trình không được vượt quá 255 ký tự")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotNull(message = "Số lượng tối đa không được để trống")
    @Positive(message = "Số lượng tối đa phải lớn hơn 0")
    private Integer maxParticipants;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    @Future(message = "Thời gian bắt đầu phải ở tương lai")
    private LocalDateTime startTime;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    @Future(message = "Thời gian kết thúc phải ở tương lai")
    private LocalDateTime endTime;

    @NotBlank(message = "Địa điểm không được để trống")
    private String location;

    @NotNull(message = "Người tổ chức không được để trống")
    private Integer hostedBy;

    @NotNull(message = "Survey id can not be null")
    private Integer surveyId;

    @NotNull(message = "Khảo sát không được để trống")
    @Valid
    private UpdateSurveyRequest survey;
}

