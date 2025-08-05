package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotiResponse {

    private UUID id;

    private String title;

    private String content;

    private AccountDto receiver;

    private Boolean isRead;

    private String notificationType;

    private Long relatedEntityId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
