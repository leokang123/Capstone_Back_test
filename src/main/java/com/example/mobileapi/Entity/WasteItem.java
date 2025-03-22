package com.example.mobileapi.Entity;

import com.example.mobileapi.DTO.UpdateWasteItem;
import com.example.mobileapi.Enum.WasteStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "waste_items")
public class WasteItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String wasteType;

    private String location;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime selectedDate;

    private String selectedDevice;

    // 폐기물 상태 추가 (ENUM)
    @Enumerated(EnumType.STRING) // DB에 ENUM을 문자열(VARCHAR)로 저장
    @Column(nullable = false)
    private WasteStatus status;

    @ManyToOne
    @JoinColumn(name = "waste_storages_id", nullable = false) // 외래키(FK) 컬럼 이름 지정
    private WasteStorage wasteStorage;

    // WasteDetail과의 OneToMany 관계 설정
    @OneToMany(mappedBy = "wasteItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WasteDetail> wasteDetails;

    @Column(nullable = false)
    private String registrantName;

    // 기본 생성자에서 상태를 COLLECTING으로 설정
    public WasteItem() {
        this.status = WasteStatus.COLLECTING; // 기본값: 수집됨 (COLLECTED)
        this.wasteDetails = new ArrayList<>();
    }

}
