package com.zipnivasa.backend.modules.user.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "messowners")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessOwner {

    @Id
    private String id;

    private String userId;

    private String messName;
    private String messLocation;
    private Integer messCapacity;
    private String messType;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
