package com.example.mobileapi.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateWasteDetail {
    private Long id;
    private String wasteDetails;
    private String date;
}