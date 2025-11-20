package com.zipnivasa.backend.modules.auth.dto;

import lombok.Data;

/**
 * Equivalent of your req.body in register().
 * We keep role-specific fields generic as "roleBody".
 */
@Data
public class RegisterRequest {

    private String role;   // tenant, pgowner, messowner, laundry
    private String email;
    private String password;
    private String name;
    private String phone;

    // Tenant-specific
    private String professionType; // "student" or "job"
    private String collegeName;
    private String course;
    private String year;
    private String companyName;
    private String workLocation;
    private String jobRole;
    private String city;

    // PG Owner specific
    private String pgName;
    private String pgLocation;
    private Integer pgCapacity;
    private String pgFacilities;

    // Mess Owner specific
    private String messName;
    private String messLocation;
    private Integer messCapacity;
    private String messType;

    // Laundry Owner specific
    private String laundryName;
    private String serviceLocation;
    private Double ratePerKg;
}
