package com.example.mobileapi.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeaconRequest {
    private Long id;
    private String macAddress;
    private Long hospitalId;
    private Boolean isUsed;
}
