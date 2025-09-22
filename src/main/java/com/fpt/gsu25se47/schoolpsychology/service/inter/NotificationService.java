package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.NotiRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.NotiResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.request.NotiSettingRequest;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    NotiResponse saveNotification(NotiRequest request);

    List<NotiResponse> getAllNotificationsByAccountId(Integer accountId);

    void readMessage(UUID notificationId);

    void sendNotification(String username, String destination, Object payload);

    void sendNotificationSetting(NotiSettingRequest request);
}
