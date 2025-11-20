package com.zipnivasa.backend.modules.mess.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Equivalent to Node Mess.js schema.
 */
@Document(collection = "messes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mess {

    @Id
    private String id;

    private String messOwnerId;  // User._id

    private String title;
    private String description;
    private String location;
    private Double price;

    private String type; // Veg, Non-Veg, Both
    private Integer capacity;

    @Builder.Default
    private List<String> menu = new ArrayList<>();

    private String contact;

    @Builder.Default
    private List<String> images = new ArrayList<>();

    // Ratings array
    @Builder.Default
    private List<Rating> ratings = new ArrayList<>();

    // Special Today object
    @Builder.Default
    private SpecialToday specialToday = new SpecialToday();

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // ---- Nested Subdocuments -----

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Rating {
        private String studentId;
        private Integer stars;
        private String comment;
        private Instant date;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpecialToday {
        private String lunch = "";
        private String dinner = "";
        private String imageUrl = "";
        private Instant date = Instant.now();
    }
}
