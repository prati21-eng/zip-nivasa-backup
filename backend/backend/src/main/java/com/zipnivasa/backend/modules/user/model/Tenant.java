package com.zipnivasa.backend.modules.user.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Equivalent of Tenant.js
 */
@Document(collection = "tenants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {

    @Id
    private String id;

    // reference to User.id
    private String userId;

    // "student" or "job"
    private String professionType;

    // student fields
    private String collegeName;
    private String course;
    private String year;

    // working professional fields
    private String companyName;
    private String workLocation;
    private String jobRole;

    private String city;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
