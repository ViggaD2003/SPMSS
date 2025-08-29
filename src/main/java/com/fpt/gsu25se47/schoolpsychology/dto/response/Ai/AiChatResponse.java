package com.fpt.gsu25se47.schoolpsychology.dto.response.Ai;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AiChatResponse {

    private String userMessage;
    private String aiMessage;
    private String sessionId;
    private String conversationId;
}
