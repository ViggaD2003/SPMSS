package com.fpt.gsu25se47.schoolpsychology.configuration;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineTrackerUser {

    // Set lưu tất cả username đang online
    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    // Khi user online
    public void setOnline(String username) {
        onlineUsers.add(username);
    }

    // Khi user offline
    public void setOffline(String username) {
        onlineUsers.remove(username);
    }

    // Lấy danh sách user đang online
    public Set<String> getOnlineUsers() {
        return Collections.unmodifiableSet(onlineUsers);
    }
}
