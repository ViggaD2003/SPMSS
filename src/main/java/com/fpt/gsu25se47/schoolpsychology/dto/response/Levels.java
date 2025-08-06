package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Levels {
    private String level;

    private int count;
}
