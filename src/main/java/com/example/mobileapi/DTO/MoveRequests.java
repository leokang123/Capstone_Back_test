package com.example.mobileapi.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MoveRequests {
    private Long stepId;
    private List<MoveRequest> wasteMoveRequests;
}
