package com.example.mobileapi.DTO;

import com.example.mobileapi.Entity.WasteItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WasteItemDetailRequest {
    private Long itemId;
    private String wasteType;
    private String registrantName;
    private String selectedDate;
    private String selectedDevice;
}
