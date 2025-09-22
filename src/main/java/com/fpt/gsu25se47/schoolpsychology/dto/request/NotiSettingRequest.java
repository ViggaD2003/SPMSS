package com.fpt.gsu25se47.schoolpsychology.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotiSettingRequest {

    private int entityId;

    private String title;

    private String content;

    private String notificationType;

    private Boolean notifyTeacher;

    private Boolean notifyParent;

    private Boolean notifyCounselor;
}
