package com.example.mobileapi.Controller;

import com.example.mobileapi.DTO.*;
import com.example.mobileapi.Entity.*;
import com.example.mobileapi.Service.WasteService;
import com.example.mobileapi.Utils.CustomUserDetails;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("waste")
public class WasteController {
    WasteService wasteService;

    @Autowired
    public WasteController(WasteService wasteService) {
        this.wasteService = wasteService;
    }
    @PostMapping("get_wastelist_by_name")
    public List<WasteItemResponse> findByWasteType(@RequestBody SearchRequest searchMessage) {
        List<WasteItem> wasteList =  wasteService.findByWasteType(searchMessage.getWasteType());
        return wasteList.stream()
                .map(WasteItemResponse::new) // WasteItemResponse 생성자 호출하여 매핑
                .sorted(Comparator.comparing(WasteItemResponse::getSelectedDate).reversed()) // 날짜 기준 내림차순 정렬
                .toList();
    }

    @PostMapping("register")
    public ResponseEntity<RegisterResponse> saveWasteItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody WasteItemRequest wasteItem
    ) {
        User user = userDetails.user();
        wasteService.registerWasteItem(wasteItem, user);
        return ResponseEntity.ok(new RegisterResponse("Waste item registered successfully"));
    }

    @GetMapping("get_wastelist")
    public ResponseEntity<List<WasteItemResponse>> getWasteList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.user();
        List<WasteItemResponse> wasteList = wasteService.findAllWasteItemsByHospital(user).stream()
                .map(WasteItemResponse::new) // WasteItemResponse 생성자 호출하여 매핑
                .sorted(Comparator.comparing(WasteItemResponse::getSelectedDate).reversed()) // 날짜 기준 내림차순 정렬
                .toList();
        return ResponseEntity.ok(wasteList);
    }

    @PostMapping("get_filtered_wastelist")
    public ResponseEntity<List<WasteItemResponse>> getFilteredWasteList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody SearchRequest searchMessage
    ) {
        User user = userDetails.user();
        List<WasteItem> wasteList = wasteService.findFilteredWasteItems(searchMessage, user);

        List<WasteItemResponse> wasteResponseList = wasteList.stream()
                .map(WasteItemResponse::new) // WasteItemResponse 생성자 호출하여 매핑
                .sorted(Comparator.comparing(WasteItemResponse::getSelectedDate).reversed()) // 날짜 기준 내림차순 정렬
                .toList();
        return ResponseEntity.ok(wasteResponseList);
    }

    @PostMapping("waste_items_next_step")
    public ResponseEntity<RegisterResponse> saveWasteItemNextStep(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MoveRequests wasteMoveRequests
    ) {
        try {
            User user = userDetails.user();
            wasteService.moveWasteItemsToNextStep(wasteMoveRequests, user);
        } catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new RegisterResponse(e.getMessage()));
        }
        log.info("Waste item moved to next step");
        return ResponseEntity.ok(new RegisterResponse("Waste items next step registered successfully"));
    }

    @GetMapping("get_detail_waste_item")
    public ResponseEntity<WasteItem> findWasteItem(@RequestParam Long itemId) {
        try {
            WasteItem wasteItem = wasteService.findWasteItemById(itemId);
            return ResponseEntity.ok(wasteItem); // 성공 시 200 OK + WasteItem 반환
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); // 실패 시 404 NOT FOUND 반환
        }
    }

    @GetMapping("check_item_status")
    public ResponseEntity<Boolean> checkWasteItem(@RequestParam Long itemId) {
        boolean exists = wasteService.checkItemStatus(itemId);
        log.info("Waste item exists : " + exists);
        return ResponseEntity.ok(exists);
    }

    @PutMapping("update_item")
    public ResponseEntity<Boolean> updateWasteItem(@RequestBody UpdateWasteItem wasteItem) {
        wasteService.updateWasteItem(wasteItem);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("delete_item")
    public ResponseEntity<Boolean> deleteWasteItem(@RequestParam Long itemId) {
        boolean exists = wasteService.checkItemStatus(itemId);
        if (exists) {
            wasteService.deleteWasteItem(itemId);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

}
