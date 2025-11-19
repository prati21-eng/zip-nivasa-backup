package com.zipnivasa.backend.modules.user.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Equivalent of Backend/models/User.js
 */
@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;  // Mongo ObjectId as String

    private String name;

    @Indexed(unique = true)
    private String email;

    private String phone;

    private String password; // hashed (BCrypt)

    // "tenant", "pgowner", "messowner", "laundry"
    private String role;

    // Id of role-specific document (Tenant/PGOwner/etc.)
    private String roleId;

    // "Tenant", "PGOwner", "MessOwner", "LaundryOwner"
    private String roleModel;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
