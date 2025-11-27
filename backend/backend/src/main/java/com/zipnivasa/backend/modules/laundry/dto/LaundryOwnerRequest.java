package com.zipnivasa.backend.modules.laundry.dto;

import lombok.Data;

@Data
public class LaundryOwnerRequest {

    private String userId;
    private String laundryName;
    private String serviceLocation;
    private Double ratePerKg;
}
