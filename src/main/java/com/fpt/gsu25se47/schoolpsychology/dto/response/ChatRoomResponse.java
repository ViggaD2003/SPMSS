package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatRoomResponse {
    private Integer id;

    private String email;

    private LocalDateTime timeStamp;

    private String roleRoom;
}
