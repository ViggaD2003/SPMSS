package com.fpt.gsu25se47.schoolpsychology.dto.response.Cases;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CaseProfileDto {
    private Integer id;
    private Integer hostBy;
    private Status status;
}
