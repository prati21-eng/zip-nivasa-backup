package com.zipnivasa.backend.modules.messowner.dto;

import lombok.Data;

@Data
public class MessOwnerRequest {

    private String userId;
    private String messName;
    private String messLocation;
    private Integer messCapacity;
    private String messType;
}
