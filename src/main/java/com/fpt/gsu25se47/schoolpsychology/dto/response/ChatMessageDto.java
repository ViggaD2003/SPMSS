package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDto {

    private String sender;

    private String message;

    private LocalDateTime timestamp;
}
