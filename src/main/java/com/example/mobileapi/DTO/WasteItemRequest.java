package com.example.mobileapi.DTO;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WasteItemRequest {
    private String wasteType;
    private String wasteDetails;
    private String location;
    private String selectedDate;
    private String selectedDevice;
    private Long storageId;
}