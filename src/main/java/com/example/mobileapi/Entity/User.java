package com.example.mobileapi.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 ID
    private Long id;

    @Column(nullable = false, unique = true) // 아이디 중복 방지
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password; // 비밀번호 (해싱하여 저장)

    @Column(nullable = true, unique = true) // 이메일 중복 방지
    private String email;

    @Column(nullable = true) // 사용자 이름
    private String name;

    @Column(nullable = true, unique = true) // 전화번호 중복 방지
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "hospitals_id", nullable = false) // 외래키(FK) 컬럼 이름 지정
    private Hospital hospital;

    @ManyToOne
    @JoinColumn(name = "roles_id", nullable = false) // 외래키(FK) 컬럼 이름 지정
    private Role role;

}
