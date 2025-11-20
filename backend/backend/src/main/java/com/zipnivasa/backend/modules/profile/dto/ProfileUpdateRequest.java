package com.zipnivasa.backend.modules.profile.dto;

import lombok.Data;
import java.util.Map;

/**
 * Represents the payload of /api/profile/update
 * {
 *   "name": "...",
 *   "phone": "...",
 *   "roleData": { ... role specific fields ... }
 * }
 */
@Data
public class ProfileUpdateRequest {

    private String name;
    private String phone;

    // dynamic role-specific fields
    private Map<String, Object> roleData;
}
