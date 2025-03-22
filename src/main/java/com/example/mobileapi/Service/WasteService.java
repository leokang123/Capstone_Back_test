package com.example.mobileapi.Service;

import com.example.mobileapi.DTO.*;
import com.example.mobileapi.Entity.*;
import com.example.mobileapi.Enum.WasteStatus;
import com.example.mobileapi.Repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WasteService {
    WasteRepository wasteRepository;
    WasteStorageRepository wasteStorageRepository;
    WasteDetailRepository wasteDetailRepository;
    UserRepository userRepository;
    BeaconRepository beaconRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    @Autowired
    public WasteService(WasteRepository wasteRepository,
                        UserRepository userRepository,
                        WasteStorageRepository wasteStorageRepository,
                        BeaconRepository beaconRepository,
                        WasteDetailRepository wasteDetailRepository
    ) {
        this.wasteRepository = wasteRepository;
        this.userRepository = userRepository;
        this.wasteStorageRepository = wasteStorageRepository;
        this.beaconRepository = beaconRepository;
        this.wasteDetailRepository = wasteDetailRepository;
    }

    // wasteItem 다 찾는 서비스 유저 정보 x
    public List<WasteItem> findAllWasteItems() {
        return wasteRepository.findAll();
    }

    public List<WasteItem> findAllWasteItemsByHospital(User user) {
        Hospital hospital = user.getHospital();
        return wasteRepository.findAllByHospital(hospital.getId());
    }

    //  wasteItem을 wasteType으로 찾는 서비스 유저 정보 x
    public List<WasteItem> findByWasteType(String wasteType) {
        return wasteRepository.findByWasteTypeContaining(wasteType);
    }

    public List<WasteItem> findFilteredWasteItems(SearchRequest searchMessage, User user) {

        // `selectedDate`가 null 또는 빈 값이면 현재 시간 사용
        LocalDateTime selectedDate = (searchMessage.getCombineDate() == null || searchMessage.getCombineDate().isEmpty())
                ? LocalDateTime.now().minusYears(100)  // 100년 전 날짜로 설정 (DB에서 무조건 포함되도록)
                : LocalDateTime.parse(searchMessage.getCombineDate(), formatter);

        // `endDate`가 null 또는 `LocalDateTime.MIN`이면 100년 후로 설정
        LocalDateTime endDate = (searchMessage.getCombineDate() == null || searchMessage.getCombineDate().isEmpty())
                ? LocalDateTime.now().plusYears(100)  // 100년 후 날짜로 설정
                : selectedDate.plusDays(10);

        log.info("Waste Type : " + searchMessage.getWasteType());
        log.info("Selected date is {}", selectedDate);
        log.info("End date is {}", endDate);
        log.info("Status is {}", searchMessage.getWasteStatus());
        return wasteRepository.findFilteredWasteItems(
                searchMessage.getWasteType(),
                searchMessage.getRegistrantName(),
                selectedDate,
                endDate,
                user.getHospital().getId(),
                searchMessage.getSelectedDevice(),
                searchMessage.getWasteStatus()
        );
    }

    // wasteItem 등록 서비스
    public void registerWasteItem(WasteItemRequest wasteItemRequest, User user) {
        Optional<WasteStorage> wasteStorage = wasteStorageRepository.findById(wasteItemRequest.getStorageId());

        WasteItem wasteItem = new WasteItem();

        // storage 세팅
        wasteStorage.ifPresent(wasteItem::setWasteStorage);

        // 폐기물 이력 세팅
        WasteDetail wasteDetail = new WasteDetail();
        wasteDetail.setUser(user);

        wasteDetail.setWasteItem(wasteItem);
        wasteDetail.setStatus(wasteItem.getStatus());
        wasteDetail.setWasteDetails(wasteItemRequest.getWasteDetails());

        // `selectedDate`를 `LocalDateTime`으로 변환 (입력값이 `null`이면 현재 시간 설정)
        LocalDateTime dateTime = (wasteItemRequest.getSelectedDate() == null || wasteItemRequest.getSelectedDate().isEmpty())
                ? LocalDateTime.now() // 기본값: 현재 시간
                : LocalDateTime.parse(wasteItemRequest.getSelectedDate(), formatter);

        wasteDetail.setDate(dateTime);
        wasteItem.getWasteDetails().add(wasteDetail);

        // 그 외의 것들 세팅
        wasteItem.setRegistrantName(user.getName());
        wasteItem.setWasteType(wasteItemRequest.getWasteType());
        wasteItem.setLocation(wasteItemRequest.getLocation());
        wasteItem.setSelectedDate(dateTime);

        wasteItem.setSelectedDevice(wasteItemRequest.getSelectedDevice());
        wasteRepository.save(wasteItem);
    }

    @Transactional
    public void moveWasteItemsToNextStep(MoveRequests moveRequests, User user) {
        List<MoveRequest> moveRequestList = moveRequests.getWasteMoveRequests();
        for (MoveRequest request : moveRequestList) {
            // 현재 WasteItem 가져오기
            WasteItem wasteItem = wasteRepository.findById(request.getItemId())
                    .orElseThrow(() -> new IllegalStateException("해당 WasteItem이 존재하지 않습니다: " + request.getItemId()));

            // 다음 상태 가져오기
            WasteStatus nextStatus = null;
            if (moveRequests.getStepId() == 1) nextStatus = wasteItem.getStatus().getNextStep();
            else if (moveRequests.getStepId() == 2) nextStatus = wasteItem.getStatus().getLastStep();

            // WasteItem 상태 업데이트 (캐싱된 최신 상태 변경)
            wasteItem.setStatus(nextStatus);
            wasteRepository.save(wasteItem);

            // WasteDetail 기록 추가 (새로운 상태 기록 저장)
            WasteDetail newDetail = new WasteDetail();
            newDetail.setWasteItem(wasteItem);
            newDetail.setUser(user);

            // `selectedDate`를 `LocalDateTime`으로 변환 (입력값이 `null`이면 현재 시간 설정)
            LocalDateTime dateTime = (request.getDate() == null || request.getDate().isEmpty())
                    ? LocalDateTime.now() // 기본값: 현재 시간
                    : LocalDateTime.parse(request.getDate(), formatter);
            log.info("DATETIME : {}", dateTime.format(formatter));
            newDetail.setDate(dateTime);
            newDetail.setWasteDetails(request.getWasteDetails());
            newDetail.setStatus(nextStatus);
            wasteDetailRepository.save(newDetail);
        }
    }

    // 유저 정보 x
    public WasteItem findWasteItemById(Long id) {
        return wasteRepository.findById(id).orElse(null);
    }

    // 유저 정보 x
    public Boolean checkItemStatus(Long id) {
        return wasteRepository.findById(id)
                .map(wasteItem -> WasteStatus.COLLECTING.equals(wasteItem.getStatus())) // 안전한 null 비교
                .orElse(false); // ID가 존재하지 않으면 false 반환
    }

    public void updateWasteItem(UpdateWasteItem wasteItem) {
        Optional<WasteItem> _waste_item = wasteRepository.findById(wasteItem.getId());
        if (_waste_item.isPresent()) {
            WasteItem updateItem = _waste_item.get();
            updateItem.setWasteStorage(wasteItem.getWasteStorage());
            updateItem.setWasteType(wasteItem.getWasteType());
            updateItem.setLocation(wasteItem.getLocation());
            updateItem.setSelectedDevice(wasteItem.getSelectedDevice());
            updateItem.getWasteDetails().forEach(existingDetail ->
                    wasteItem.getWasteDetails().stream()
                            .filter(newDetail -> newDetail.getId().equals(existingDetail.getId()))
                            .findFirst()
                            .ifPresent(matchingDetail -> {
                                existingDetail.setWasteDetails(matchingDetail.getWasteDetails());
                            })
            );


            wasteRepository.save(updateItem);
        }
    }

    public void deleteWasteItem(Long id) {
        wasteRepository.deleteById(id); // 삭제 수행
    }
}
