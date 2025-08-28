package com.fpt.gsu25se47.schoolpsychology.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class PresenceEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    private final OnlineTrackerUser onlineTrackerUser;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username != null ) {
            onlineTrackerUser.setOffline(username);
            messagingTemplate.convertAndSend("/topic/onlineUsers",  onlineTrackerUser.getOnlineUsers());
        }
    }

}
