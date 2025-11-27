package com.zipnivasa.backend.modules.profile.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ProfileUpdateRequest {

    private String name;
    private String phone;

    private Map<String, Object> roleData;
}
