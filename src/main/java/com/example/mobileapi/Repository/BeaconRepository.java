package com.example.mobileapi.Repository;

import com.example.mobileapi.Entity.Beacon;
import com.example.mobileapi.Entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeaconRepository extends JpaRepository<Beacon, Long> {
    List<Beacon> findAllByHospital(Hospital hospital);
}
