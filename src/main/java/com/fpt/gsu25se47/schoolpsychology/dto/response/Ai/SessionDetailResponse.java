package com.fpt.gsu25se47.schoolpsychology.dto.response.Ai;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class SessionDetailResponse {

    private String id;
    private LocalDateTime createdTime;
    private LocalDateTime editedTime;
    private String name;
    private Integer userId;
    private List<MessageResponse> messageResponses;
}
