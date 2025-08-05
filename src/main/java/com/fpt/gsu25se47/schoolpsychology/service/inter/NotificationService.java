package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.NotiRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.NotiResponse;
import java.util.List;

public interface NotificationService {

    NotiResponse saveNotification(NotiRequest request);

    List<NotiResponse> getAllNotificationsByAccountId(Integer accountId);

    void sendNotification(String username, String destination, Object payload);
}
