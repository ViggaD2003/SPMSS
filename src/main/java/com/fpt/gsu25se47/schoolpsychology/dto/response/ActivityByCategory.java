package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityByCategory {
    private String category;

    private int programs;

    private int surveys;

    private int appointments;
}
