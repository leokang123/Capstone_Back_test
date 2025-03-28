package com.example.mobileapi.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "beacons")
@NoArgsConstructor
public class Beacon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) //
    private String macAddress;

    @ManyToOne
    @JoinColumn(name = "hospitals_id", nullable = false) // 외래키(FK) 컬럼 이름 지정
    private Hospital hospital;

    @Column(nullable = false) //
    private boolean isUsed;

}
