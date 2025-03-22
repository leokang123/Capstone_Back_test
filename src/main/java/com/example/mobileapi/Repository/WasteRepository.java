package com.example.mobileapi.Repository;

import com.example.mobileapi.Entity.Hospital;
import com.example.mobileapi.Entity.WasteItem;
import com.example.mobileapi.Enum.WasteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WasteRepository extends JpaRepository<WasteItem, Long> {
    Optional<WasteItem> findByWasteType(String wasteType);
    List<WasteItem> findByWasteTypeContaining(String wasteType);


    @Query("SELECT w FROM WasteItem w WHERE " +
            "(:hospitalId IS NULL OR w.wasteStorage.hospital.id = :hospitalId)" +
            "ORDER BY w.selectedDate DESC")
    List<WasteItem> findAllByHospital(
            @Param("hospitalId") Long hospitalId
    );

    @Query("SELECT w FROM WasteItem w WHERE " +
            "(:wasteType IS NULL OR :wasteType = '' OR w.wasteType LIKE CONCAT('%', :wasteType, '%')) AND " +  // ✅ 앞뒤 와일드카드 적용
            "(:registrantName IS NULL OR :registrantName = '' OR w.registrantName LIKE CONCAT('%', :registrantName, '%')) AND " +  // ✅ 앞뒤 와일드카드 적용
            "(:selectedDevice IS NULL OR :selectedDevice = '' OR w.selectedDevice LIKE CONCAT('%', :selectedDevice, '%')) AND " +  // ✅ 앞뒤 와일드카드 적용
            "(:wasteStatus IS NULL OR :wasteStatus = '' OR w.status = :wasteStatus) AND " +
            "(:hospitalId IS NULL OR w.wasteStorage.hospital.id = :hospitalId) AND " +
            "(COALESCE(:selectedDate, w.selectedDate) = w.selectedDate " +
            " OR w.selectedDate BETWEEN :selectedDate AND :endDate) " +
            "ORDER BY w.selectedDate DESC")
    List<WasteItem> findFilteredWasteItems(
            @Param("wasteType") String wasteType,
            @Param("registrantName") String registrantName,
            @Param("selectedDate") LocalDateTime selectedDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("hospitalId") Long hospitalId,
            @Param("selectedDevice") String selectedDevice,
            @Param("wasteStatus") WasteStatus wasteStatus
    );

}
