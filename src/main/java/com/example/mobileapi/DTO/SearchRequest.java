package com.example.mobileapi.DTO;

import com.example.mobileapi.Enum.WasteStatus;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {
    @Nullable
    private String wasteType;
    @Nullable
    private String registrantName;
    @Nullable
    private String selectedDate;
    @Nullable
    private String selectedTime;
    @Nullable
    private String combineDate;
    @Nullable
    private String selectedDevice;
    @Nullable
    private WasteStatus wasteStatus;
}
