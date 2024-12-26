package com.shsh.user_profile_service.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CreateUserProfileRequest {
    private String id;
    private String email;
    private String username;
}
