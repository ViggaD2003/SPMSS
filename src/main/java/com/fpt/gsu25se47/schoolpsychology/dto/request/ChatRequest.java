package com.fpt.gsu25se47.schoolpsychology.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRequest {
    private String message;
}
