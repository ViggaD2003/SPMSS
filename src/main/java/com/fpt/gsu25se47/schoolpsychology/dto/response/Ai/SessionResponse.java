package com.fpt.gsu25se47.schoolpsychology.dto.response.Ai;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SessionResponse {

    private String id;
    private LocalDateTime createdTime;
    private LocalDateTime editedTime;
    private String name;
    private Integer userId;
}
