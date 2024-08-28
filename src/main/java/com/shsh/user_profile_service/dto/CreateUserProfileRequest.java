package com.shsh.user_profile_service.dto;

import lombok.Data;

@Data
public class CreateUserProfileRequest {

    private String id;
    private String email;
    private String username;

}
