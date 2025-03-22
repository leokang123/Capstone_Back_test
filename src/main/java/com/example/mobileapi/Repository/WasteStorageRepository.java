package com.example.mobileapi.Repository;

import com.example.mobileapi.Entity.Hospital;
import com.example.mobileapi.Entity.WasteStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WasteStorageRepository extends JpaRepository<WasteStorage, Long> {
    List<WasteStorage> findAllByHospital(Hospital hospital);
}
