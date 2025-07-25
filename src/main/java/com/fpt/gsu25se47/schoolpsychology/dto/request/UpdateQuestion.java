package com.fpt.gsu25se47.schoolpsychology.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateQuestion {

    @NotNull(message = "Danh mục không được để trống")
    private Integer questionId;

    @NotNull(message = "Vui lòng nhập is active question")
    private Boolean isActive;
}
