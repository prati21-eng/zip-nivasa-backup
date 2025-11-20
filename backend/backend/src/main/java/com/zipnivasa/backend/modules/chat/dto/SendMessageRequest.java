package com.zipnivasa.backend.modules.chat.dto;

import lombok.Data;

@Data
public class SendMessageRequest {
    private String receiver;
    private String message;
}
