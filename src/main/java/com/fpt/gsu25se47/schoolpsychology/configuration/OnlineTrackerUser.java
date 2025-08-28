package com.fpt.gsu25se47.schoolpsychology.configuration;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineTrackerUser {

    // Map<roomId, Set<username>>
    private final Map<Integer, Set<String>> onlineUsersByRoom = new ConcurrentHashMap<>();

    // Khi user join room
    public void setOnline(Integer roomId, String username) {
        onlineUsersByRoom
                .computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet())
                .add(username);
    }

    // Khi user leave room
    public void setOffline(Integer roomId, String username) {
        Set<String> users = onlineUsersByRoom.get(roomId);
        if (users != null) {
            users.remove(username);
            if (users.isEmpty()) {
                onlineUsersByRoom.remove(roomId);
            }
        }
    }

    // Lấy danh sách user đang online trong room
    public Set<String> getOnlineUsers(Integer roomId) {
        return onlineUsersByRoom.getOrDefault(roomId, Collections.emptySet());
    }

    // Nếu muốn lấy tất cả user online trên toàn hệ thống
    public Map<Integer, Set<String>> getAllOnlineUsers() {
        return onlineUsersByRoom;
    }
}
