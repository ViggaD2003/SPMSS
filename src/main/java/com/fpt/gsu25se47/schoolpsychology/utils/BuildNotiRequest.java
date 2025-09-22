package com.fpt.gsu25se47.schoolpsychology.utils;

import com.fpt.gsu25se47.schoolpsychology.dto.request.NotiRequest;


public class BuildNotiRequest {

    public static NotiRequest buildNotiRequest(int entityId, String title, String content, String notificationType, String email) {
        return NotiRequest.builder()
                .title(title)
                .notificationType(notificationType)
                .relatedEntityId(entityId)
                .content(content)
                .username(email)
                .build();
    }
}
