package com.example.mobileapi.Controller;

import com.example.mobileapi.DTO.BeaconRequest;
import com.example.mobileapi.DTO.RegisterResponse;
import com.example.mobileapi.DTO.RegisterStorageRequest;
import com.example.mobileapi.Entity.*;
import com.example.mobileapi.Service.SettingService;
import com.example.mobileapi.Utils.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(("setting"))
public class SettingController {
    private final SettingService settingService;

    @Autowired
    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @PostMapping("save_beacon")
    public ResponseEntity<RegisterResponse> saveBeacon(@RequestBody BeaconRequest beacon) {
        try{
            settingService.saveBeacon(beacon);
        } catch(Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new RegisterResponse(e.getMessage()));
        }
        return ResponseEntity.ok(new RegisterResponse("Beacon saved successfully"));
    }

    @PostMapping("save_beacons")
    public ResponseEntity<RegisterResponse> saveBeacons(@RequestBody List<BeaconRequest> beacons) {
        try {
            settingService.saveBeacons(beacons); // 리스트로 저장
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new RegisterResponse(e.getMessage()));
        }
        return ResponseEntity.ok(new RegisterResponse("All beacons saved successfully"));
    }

    @GetMapping("get_beacon_list")
    public ResponseEntity<List<Beacon>> getBeaconList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.user();
        List<Beacon> beacons = settingService.getBeaconsByHospital(user.getHospital());
        return ResponseEntity.ok(beacons);
    }

    @PostMapping("save_roles")
    public ResponseEntity<RegisterResponse> saveRoles(@RequestBody List<Role> roles) {
        try {
            settingService.saveRoles(roles);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new RegisterResponse(e.getMessage()));
        }
        return ResponseEntity.ok(new RegisterResponse("All roles saved successfully"));
    }

    @GetMapping("get_storage_list")
    public ResponseEntity<List<WasteStorage>> getStorageListByHospitalId(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.user();
        List<WasteStorage> wasteStorageList =  settingService.getStoragesByHospital(user.getHospital());
        return ResponseEntity.ok(wasteStorageList);
    }


    @PostMapping("register_storage")
    public ResponseEntity<RegisterResponse> saveStorage(@RequestBody RegisterStorageRequest registerStorageRequest) {
        try{
            settingService.registerWasteStorage(registerStorageRequest);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(new RegisterResponse(e.getMessage()));
        }
        return ResponseEntity.ok(new RegisterResponse("Waste storage registered successfully"));
    }

    @PostMapping("register_storages")
    public ResponseEntity<RegisterResponse> saveStorages(@RequestBody List<RegisterStorageRequest> storageRequests) {
        try {
            settingService.registerWasteStorages(storageRequests);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RegisterResponse(e.getMessage()));
        }
        return ResponseEntity.ok(new RegisterResponse("All waste storages registered successfully"));
    }

    @PostMapping("save_hospital")
    public ResponseEntity<RegisterResponse> saveHospital(@RequestBody Hospital hospital) {
        try{
            settingService.saveHospital(hospital);
        } catch(Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new RegisterResponse(e.getMessage()));
        }
        return ResponseEntity.ok(new RegisterResponse("Hospital saved successfully"));
    }

    @PostMapping("save_hospitals")
    public ResponseEntity<RegisterResponse> saveHospitals(@RequestBody List<Hospital> hospitals) {
        try{
            settingService.saveHospitals(hospitals);
        } catch(Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new RegisterResponse(e.getMessage()));
        }
        return ResponseEntity.ok(new RegisterResponse("Hospital list saved successfully"));
    }

    @GetMapping("get_hospital_list")
    public ResponseEntity<List<Hospital>> getHospitalList() {
        List<Hospital> hospitals = settingService.getHospitals();
        return ResponseEntity.ok(hospitals);
    }



}
