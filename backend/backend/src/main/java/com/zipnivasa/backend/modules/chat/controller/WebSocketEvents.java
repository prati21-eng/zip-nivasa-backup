package com.zipnivasa.backend.modules.chat.controller;

import com.zipnivasa.backend.modules.chat.service.ChatWebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;

@Component
@RequiredArgsConstructor
public class WebSocketEvents {

    private final ChatWebSocketService chatWebSocketService;

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = accessor.getFirstNativeHeader("user-id");
        String sessionId = accessor.getSessionId();

        if (userId != null) {
            chatWebSocketService.registerUser(userId, sessionId);
        }
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        chatWebSocketService.removeUser(sessionId);
    }
}
