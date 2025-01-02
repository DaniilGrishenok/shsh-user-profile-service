package com.shsh.user_profile_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetStatusResponse {
    private String userId;
    private  String status;
    private  String lastSeen;


}
