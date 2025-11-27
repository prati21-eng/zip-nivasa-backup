package com.zipnivasa.backend.modules.user.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "laundryowners")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaundryOwner {

    @Id
    private String id;

    private String userId;
    private String laundryName;
    private String serviceLocation;
    private Double ratePerKg;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
