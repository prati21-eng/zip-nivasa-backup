package com.zipnivasa.backend.modules.pg.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "pgs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PG {

    @Id
    private String id;

    private String title;
    private String propertyType;
    private String location;
    private String address;

    private Double monthlyRent;
    private Double deposit;
    private String occupancyType;

    @Builder.Default
    private List<String> amenities = new ArrayList<>();

    private String description;

    @Builder.Default
    private List<String> images = new ArrayList<>();

    // owner = User._id
    private String owner;

    @Builder.Default
    private Integer views = 0;

    @Builder.Default
    private Integer inquiries = 0;

    @Builder.Default
    private Integer beds = 1;

    @Builder.Default
    private Boolean available = true;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
