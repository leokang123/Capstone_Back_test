package com.example.mobileapi.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveRequest {
    private Long itemId;
    private Long userId;
    private String wasteDetails;
    private String date;
}
