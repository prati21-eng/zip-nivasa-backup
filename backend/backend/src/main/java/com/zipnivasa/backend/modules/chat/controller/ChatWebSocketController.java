package com.zipnivasa.backend.modules.chat.controller;

import com.zipnivasa.backend.modules.chat.dto.MessageResponse;
import com.zipnivasa.backend.modules.chat.dto.SendMessageRequest;
import com.zipnivasa.backend.modules.chat.service.ChatService;
import com.zipnivasa.backend.modules.chat.service.ChatWebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;
    private final ChatWebSocketService webSocketService;

    @MessageMapping("/chat/send")
    public void sendMessage(@Payload SendMessageRequest req,
                            @Header("user-id") String senderId) {

        MessageResponse saved = chatService.send(senderId, req);

        webSocketService.sendPrivateMessage(req.getReceiver(), saved);

        webSocketService.sendPrivateMessage(senderId, saved);
    }

    @MessageMapping("/chat/typing")
    public void typing(@Header("user-id") String sender,
                       @Header("receiver-id") String receiver) {

        webSocketService.sendTyping(receiver, sender);
    }

    @MessageMapping("/chat/stop-typing")
    public void stopTyping(@Header("user-id") String sender,
                           @Header("receiver-id") String receiver) {

        webSocketService.sendStopTyping(receiver, sender);
    }
}
