package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Priority;
import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgressTrend;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCaseRequest {

    @NotNull(message = "Priority không được để trống")
    private Priority priority;

    @NotNull(message = "Status không được để trống")
    private Status status;

    @NotNull(message = "ProgressTrend không được để trống")
    private ProgressTrend progressTrend;

    @NotNull(message = "CurrentLevelId không được để trống")
    private Integer currentLevelId;
}
