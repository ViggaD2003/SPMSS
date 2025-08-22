//package com.fpt.gsu25se47.schoolpsychology.configuration;
//
//import com.fpt.gsu25se47.schoolpsychology.service.inter.NotificationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.messaging.SessionConnectEvent;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//
//@Component
//@RequiredArgsConstructor
//public class PresenceEventListener {
//
//    private final OnlineTrackerUser onlineUserTracker;
//
//    private final NotificationService notificationService;
//
//    @EventListener
//    public void handleSessionConnected(SessionConnectEvent event) {
//        String username = event.getUser().getName();
//        onlineUserTracker.setOnline(username);
//
////        List<String> connections = relationshipService.getConnectionUsernames(username);
////        connections.forEach(conn ->
//////                messagingTemplate.convertAndSendToUser(
//////                        conn, "/queue/status", new StatusEvent(username, true))
////                        notificationService.sendNotification()
////        );
//    }
//
//    @EventListener
//    public void handleSessionDisconnect(SessionDisconnectEvent event) {
//        String username = event.getUser().getName();
//        onlineUserTracker.setOffline(username);
//    }
//}
