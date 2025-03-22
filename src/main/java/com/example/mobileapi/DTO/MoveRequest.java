package com.example.mobileapi.DTO;

import lombok.Getter;
import lombok.Setter;

/**
 * userId 빼야함
 */
@Getter
@Setter
public class MoveRequest {
    private Long itemId;
    private String wasteDetails;
    private String date;
}
