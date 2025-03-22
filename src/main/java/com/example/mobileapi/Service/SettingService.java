package com.example.mobileapi.Service;


import com.example.mobileapi.DTO.BeaconRequest;
import com.example.mobileapi.DTO.RegisterStorageRequest;
import com.example.mobileapi.Entity.Beacon;
import com.example.mobileapi.Entity.Hospital;
import com.example.mobileapi.Entity.Role;
import com.example.mobileapi.Entity.WasteStorage;
import com.example.mobileapi.Repository.BeaconRepository;
import com.example.mobileapi.Repository.HospitalRepository;
import com.example.mobileapi.Repository.RoleRepository;
import com.example.mobileapi.Repository.WasteStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SettingService {
    private final BeaconRepository beaconRepository;
    private final RoleRepository roleRepository;
    private final HospitalRepository hospitalRepository;
    private final WasteStorageRepository wasteStorageRepository;

    @Autowired
    public SettingService(BeaconRepository beaconRepository, RoleRepository roleRepository, HospitalRepository hospitalRepository, WasteStorageRepository wasteStorageRepository) {
        this.beaconRepository = beaconRepository;
        this.roleRepository = roleRepository;
        this.hospitalRepository = hospitalRepository;
        this.wasteStorageRepository = wasteStorageRepository;
    }

    public Beacon getBeaconById(Long id) {
        return beaconRepository.findById(id).orElse(null);
    }
    public List<Beacon> getBeacons() {
        return beaconRepository.findAll();
    }

    public List<Beacon> getBeaconsByHospital(Hospital hospital) {
        return beaconRepository.findAllByHospital(hospital);
    }

    public void saveBeacon(BeaconRequest beacon) {
        Hospital hs = hospitalRepository.findById(beacon.getHospitalId()).orElse(null);
        Beacon newBeacon = new Beacon();
        newBeacon.setHospital(hs);
        newBeacon.setId(beacon.getId());
        newBeacon.setMacAddress(beacon.getMacAddress());
        newBeacon.setUsed(false);

        beaconRepository.save(newBeacon);
    }
    // 저장소 리스트형태로 다저장
    public void saveBeacons(List<BeaconRequest> beacons) {
        List<Beacon> beaconEntities = new ArrayList<>();

        for (BeaconRequest req : beacons) {
            // 1. hospitalId로 병원 엔티티 조회
            Hospital hospital = hospitalRepository.findById(req.getHospitalId())
                    .orElseThrow(() -> new IllegalArgumentException("Hospital not found with id: " + req.getHospitalId()));

            // 2. Beacon 엔티티 생성 및 매핑
            Beacon beacon = new Beacon();
            beacon.setId(req.getId());
            beacon.setMacAddress(req.getMacAddress());
            beacon.setHospital(hospital);
            beacon.setUsed(false); // null-safe

            beaconEntities.add(beacon);
        }

        // 3. 일괄 저장
        beaconRepository.saveAll(beaconEntities);
    }



    public List<WasteStorage> findAllWasteStorages() {
        return wasteStorageRepository.findAll();
    }

    public List<WasteStorage> getStoragesByHospital(Hospital hospital) {
        return wasteStorageRepository.findAllByHospital(hospital);
    }

    public void registerWasteStorage(RegisterStorageRequest wasteStorageRequest) {
        WasteStorage wasteStorage = new WasteStorage();
        Beacon beacon = beaconRepository.findById(wasteStorageRequest.getBeaconId()).orElse(null);
        Hospital hs = hospitalRepository.findById(wasteStorageRequest.getHospitalId()).orElse(null);
        wasteStorage.setBeacon(beacon);
        wasteStorage.setStorageName(wasteStorageRequest.getStorageName());
        wasteStorage.setHospital(hs);

        wasteStorageRepository.save(wasteStorage);
    }

    // 저장소 리스트형태로 다저장
    public void registerWasteStorages(List<RegisterStorageRequest> wasteStoragesRequest) {
        List<WasteStorage> wasteStorages = new ArrayList<>();

        for (RegisterStorageRequest request : wasteStoragesRequest) {
            Beacon beacon = beaconRepository.findById(request.getBeaconId()).orElse(null);
            Hospital hs = hospitalRepository.findById(request.getHospitalId()).orElse(null);

            WasteStorage wasteStorage = new WasteStorage();
            wasteStorage.setBeacon(beacon);
            wasteStorage.setStorageName(request.getStorageName());
            wasteStorage.setHospital(hs);

            wasteStorages.add(wasteStorage);
        }
        wasteStorageRepository.saveAll(wasteStorages); // 한 번에 저장
    }

    public void saveRoles(List<Role> roles) {
        roleRepository.saveAll(roles);
    }

    public Role getRole(Long roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public void saveHospital(Hospital hospital) {
        hospitalRepository.save(hospital);
    }

    public void saveHospitals(List<Hospital> hospitals) {
        hospitalRepository.saveAll(hospitals);
    }

    public Hospital getHospitalById(Long hospitalId) {
        return hospitalRepository.findById(hospitalId).orElse(null);
    }

    public List<Hospital> getHospitals() {
        return hospitalRepository.findAll();
    }


}
