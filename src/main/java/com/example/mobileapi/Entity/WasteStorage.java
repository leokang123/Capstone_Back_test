package com.example.mobileapi.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "waste_storages")
@NoArgsConstructor
public class WasteStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 ID
    private Long id;

    @Column(nullable = false) // 아이디 중복 방지
    private String storageName;

    @OneToOne
    @JoinColumn(name = "beacon_id", unique = true, nullable = false) // beacon_id가 FK (null 허용 가능)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Beacon beacon;
}