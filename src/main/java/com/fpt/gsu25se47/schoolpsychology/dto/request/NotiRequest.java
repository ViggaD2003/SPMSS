package com.fpt.gsu25se47.schoolpsychology.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotiRequest {

    private String title;

    private String content;

    private String username;

}
