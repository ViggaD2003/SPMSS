package com.fpt.gsu25se47.schoolpsychology.dto.response.Ai;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageResponse {
    private String conversationId;
    private String content;
    private String type;
    private LocalDateTime timestamp;
}
