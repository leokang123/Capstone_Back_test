package com.example.mobileapi.DTO;

import com.example.mobileapi.Entity.WasteDetail;
import com.example.mobileapi.Entity.WasteStorage;
import com.example.mobileapi.Enum.WasteStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateWasteItem {
    private Long id;
    private String wasteType;
    private String location;
    private String selectedDate;
    private String selectedDevice;
    private WasteStorage wasteStorage;
    private List<UpdateWasteDetail> wasteDetails;
}
