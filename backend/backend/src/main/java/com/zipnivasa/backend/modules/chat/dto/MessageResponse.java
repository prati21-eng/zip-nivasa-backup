package com.zipnivasa.backend.modules.chat.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class MessageResponse {
    private String id;
    private String sender;
    private String receiver;
    private String message;
    private Instant createdAt;
    private Instant readAt;
}
