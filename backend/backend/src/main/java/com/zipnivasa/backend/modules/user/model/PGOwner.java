package com.zipnivasa.backend.modules.user.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "pgowners")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PGOwner {

    @Id
    private String id;

    private String userId;

    private String pgName;
    private String pgLocation;
    private Integer pgCapacity;
    private String pgFacilities;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
