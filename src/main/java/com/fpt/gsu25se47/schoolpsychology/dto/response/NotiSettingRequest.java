package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotiSettingRequest {

    private int entityId;

    private Boolean notifyTeacherOrCounselor;

    private Boolean notifyParent;

}
