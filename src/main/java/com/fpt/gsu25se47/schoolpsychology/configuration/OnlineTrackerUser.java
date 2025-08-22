//package com.fpt.gsu25se47.schoolpsychology.configuration;
//
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class OnlineTrackerUser {
//
//    private final Map<String, Boolean> onlineUsers = new ConcurrentHashMap<>();
//
//    public void setOnline(String username) {
//        onlineUsers.put(username, true);
//    }
//
//    public void setOffline(String username) {
//        onlineUsers.remove(username);
//    }
//
////    public boolean isOnline(String username) {
////        return onlineUsers.containsKey(username);
////    }
//
//    public Set<String> getAllOnlineUsers() {
//        return onlineUsers.keySet();
//    }
//}
