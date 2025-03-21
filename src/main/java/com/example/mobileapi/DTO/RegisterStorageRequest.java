package com.example.mobileapi.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterStorageRequest {
    private String storageName;
    private Long beaconId;
}
