package com.example.mobileapi.Entity;

import com.example.mobileapi.Enum.WasteStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "waste_details")
@NoArgsConstructor
public class WasteDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "waste_items_id", nullable = false) // 외래키(FK) 컬럼 이름 지정
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private WasteItem wasteItem;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    @Column
    private String wasteDetails;

    @Column
    private LocalDateTime date;

    // 폐기물 상태 추가 (ENUM)
    @Enumerated(EnumType.STRING) // DB에 ENUM을 문자열(VARCHAR)로 저장
    @Column(nullable = false)
    private WasteStatus status;
}
