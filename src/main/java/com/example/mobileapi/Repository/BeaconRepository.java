package com.example.mobileapi.Repository;

import com.example.mobileapi.Entity.Beacon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeaconRepository extends JpaRepository<Beacon, Long> {
}
