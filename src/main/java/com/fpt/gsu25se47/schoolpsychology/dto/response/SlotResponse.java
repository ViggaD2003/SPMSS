package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SlotResponse {

    private Integer id;

    private String status;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String fullName;

    private String roleName;

}
