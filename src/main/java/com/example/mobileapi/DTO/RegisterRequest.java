package com.example.mobileapi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @JsonProperty("username")
    private String username;

    private String password; // ✅ 비밀번호 (해싱하여 저장)

    private String email;

    private String name;

    private String phoneNumber;

    private Long selectedHospitalId;

    private Long roleId;

    public RegisterRequest() {
        this.roleId = 0L;
    }

}
