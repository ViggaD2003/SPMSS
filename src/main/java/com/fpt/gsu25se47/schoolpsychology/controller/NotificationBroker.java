package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.NotiRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ChatMessageDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.NotiResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.NotiSettingRequest;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ChatService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class NotificationBroker {

    private final NotificationService notificationService;

    private final ChatService chatService;

    private final SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/send")
    public void sendMessage(NotiRequest notiRequest) {
        NotiResponse response = notificationService.saveNotification(notiRequest);
        notificationService.sendNotification(notiRequest.getUsername(), "/queue/notifications", response);
    }

    @MessageMapping("/noti-setting")
    public void sendNotiSetting(NotiSettingRequest notiSettingRequest) {
        notificationService.sendNotificationSetting(notiSettingRequest);
    }

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable Integer roomId, ChatMessageDto chatMessage) {
        chatService.saveMessage(chatMessage, roomId);

        messagingTemplate.convertAndSend("/topic/chat/" + roomId, chatMessage);
    }

    @MessageMapping("/chat.addUser/{roomId}")
    public void addUser(
            @Payload ChatMessageDto chatMessage,
            SimpMessageHeaderAccessor headerAccessor,
            @DestinationVariable Integer roomId
    ) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("roomId", roomId);
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, chatMessage);
    }

}
