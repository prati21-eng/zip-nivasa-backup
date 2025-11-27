package com.zipnivasa.backend.modules.pg.dto;

import lombok.Data;

import java.util.List;

@Data
public class PGRequest {

    private String title;
    private String propertyType;
    private String location;
    private String address;

    private Double monthlyRent;
    private Double deposit;
    private String occupancyType;

    private List<String> amenities;

    private String description;

    private String ownerId;          // from JWT principal
    private List<String> imagePaths; // e.g. /uploads/pgs/<filename>
}
