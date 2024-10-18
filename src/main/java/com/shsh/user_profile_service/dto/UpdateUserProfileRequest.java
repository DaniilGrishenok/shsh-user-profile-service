package com.shsh.user_profile_service.dto;

import com.shsh.user_profile_service.model.UserProfile;
import lombok.Data;

@Data
public class UpdateUserProfileRequest {
	private String id;
	private UserProfile userProfile;
}
