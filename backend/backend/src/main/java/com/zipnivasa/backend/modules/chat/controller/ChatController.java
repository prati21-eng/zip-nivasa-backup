package com.zipnivasa.backend.modules.chat.controller;

import com.zipnivasa.backend.common.payload.ApiResponse;
import com.zipnivasa.backend.config.JwtAuthenticationFilter;
import com.zipnivasa.backend.modules.chat.dto.SendMessageRequest;
import com.zipnivasa.backend.modules.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    private String getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationFilter.JwtUserPrincipal p =
                (JwtAuthenticationFilter.JwtUserPrincipal) auth.getPrincipal();
        return p.getId();
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody SendMessageRequest req) {
        String sender = getUserId();
        return ResponseEntity.ok(
                ApiResponse.success("Message sent", chatService.send(sender, req))
        );
    }
    @GetMapping("/history/{receiverId}")
    public ResponseEntity<?> history(@PathVariable String receiverId) {
        String me = getUserId();
        return ResponseEntity.ok(
                ApiResponse.success("Messages fetched", chatService.getHistory(me, receiverId))
        );
    }

    @GetMapping("/conversations")
    public ResponseEntity<?> conversations() {
        String me = getUserId();
        return ResponseEntity.ok(
                ApiResponse.success("Conversations fetched", chatService.getConversations(me))
        );
    }

    @PostMapping("/mark-read")
    public ResponseEntity<?> markRead(@RequestBody(required = true)
                                      java.util.Map<String, String> body) {

        String partnerId = body.get("partnerId");
        String myId = getUserId();

        chatService.markAsRead(myId, partnerId);

        return ResponseEntity.ok(ApiResponse.success("Marked as read", null));
    }
}
