package com.zipnivasa.backend.modules.mess.dto;

import lombok.Data;
import java.util.List;

@Data
public class MessRequest {

    private String messOwnerId;
    private String title;
    private String description;
    private String location;
    private Double price;
    private String type;
    private Integer capacity;
    private List<String> menu;
    private String contact;
    private List<String> images;
}
