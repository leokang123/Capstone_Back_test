package com.example.mobileapi.DTO;

import com.example.mobileapi.Entity.User;
import com.example.mobileapi.Entity.WasteDetail;
import com.example.mobileapi.Entity.WasteItem;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Setter
public class WasteItemResponse {
    private Long id;
    private String wasteType;
    private String wasteDetails;
    private String location;
    private String selectedDate;
    private String status;
    private String selectedDevice;
    private String moveStateDate;

    private Long userId; // User ID만 반환 (유저 정보 전체를 보내지 않음)
    private String registrantName;

    private Long storageId;
    private String storageName;

    public WasteItemResponse(WasteItem wasteItem) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<WasteDetail> wasteDetails = wasteItem.getWasteDetails();
        Optional<WasteDetail> matchingDetail = wasteDetails.stream()
                .filter(detail -> detail.getStatus().equals(wasteItem.getStatus()))
                .findFirst();
        WasteDetail wasteDetail = matchingDetail.orElse(null);
        this.wasteDetails = wasteDetail.getWasteDetails();
        this.userId = wasteDetail.getUser().getId();
        this.registrantName = wasteDetail.getUser().getName() + " (등록: " + wasteItem.getRegistrantName() + ")";
        this.moveStateDate = wasteDetail.getDate().format(formatter);

        this.id = wasteItem.getId();
        this.wasteType = wasteItem.getWasteType();
        this.location = wasteItem.getLocation();
        this.selectedDate = wasteItem.getSelectedDate().toString();
        this.status = wasteItem.getStatus().name();
        this.selectedDevice = wasteItem.getSelectedDevice();
        this.storageId = wasteItem.getWasteStorage().getId();
        this.storageName = wasteItem.getWasteStorage().getStorageName();
    }
}


