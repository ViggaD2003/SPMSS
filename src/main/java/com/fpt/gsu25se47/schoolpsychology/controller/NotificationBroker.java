package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.NotiRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.NotiResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class NotificationBroker {

    private final NotificationService notificationService;


    @MessageMapping("/send")
    public void sendMessage(NotiRequest notiRequest) {
        NotiResponse response = notificationService.saveNotification(notiRequest);
        notificationService.sendNotification(notiRequest.getUsername(), "/queue/notifications", response);
    }

}
