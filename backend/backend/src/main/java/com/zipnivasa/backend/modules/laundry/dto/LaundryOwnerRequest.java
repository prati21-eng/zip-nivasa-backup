package com.zipnivasa.backend.modules.laundry.dto;

import lombok.Data;

/**
 * Payload for adding or updating Laundry Owner.
 */
@Data
public class LaundryOwnerRequest {

    private String userId;
    private String laundryName;
    private String serviceLocation;
    private Double ratePerKg;
}
