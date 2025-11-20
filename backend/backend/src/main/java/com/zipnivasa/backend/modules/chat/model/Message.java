package com.zipnivasa.backend.modules.chat.model;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    private String id;

    @Indexed
    private String sender;

    @Indexed
    private String receiver;

    private String message;

    private Instant readAt;

    @CreatedDate
    private Instant createdAt;
}
