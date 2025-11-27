package com.zipnivasa.backend.modules.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This service replicates the behavior of your Node socket.io system:
 *
 * - onlineUsers Map
 * - store user ↔ session mapping
 * - broadcast online users
 * - send message events
 * - typing / stop typing events
 */
@Service
@RequiredArgsConstructor
public class ChatWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    // userId → sessionId
    private final Map<String, String> onlineUsers = new ConcurrentHashMap<>();

    // sessionId → userId
    private final Map<String, String> sessionToUser = new ConcurrentHashMap<>();


    public void registerUser(String userId, String sessionId) {
        onlineUsers.put(userId, sessionId);
        sessionToUser.put(sessionId, userId);

        broadcastOnlineUsers();
    }

    public void removeUser(String sessionId) {
        String userId = sessionToUser.remove(sessionId);
        if (userId != null) {
            onlineUsers.remove(userId);
            broadcastOnlineUsers();
        }
    }


    public void broadcastOnlineUsers() {
        Set<String> users = onlineUsers.keySet();

        messagingTemplate.convertAndSend(
                "/topic/online-users",
                users
        );
    }


    public void sendPrivateMessage(String receiverId, Object payload) {
        messagingTemplate.convertAndSend("/queue/messages/" + receiverId, payload);
    }


    public void sendTyping(String receiverId, String senderId) {
        messagingTemplate.convertAndSend("/queue/typing/" + receiverId, senderId);
    }

    public void sendStopTyping(String receiverId, String senderId) {
        messagingTemplate.convertAndSend("/queue/stop-typing/" + receiverId, senderId);
    }


    public boolean isUserOnline(String userId) {
        return onlineUsers.containsKey(userId);
    }

    public String getSessionId(String userId) {
        return onlineUsers.get(userId);
    }
}
