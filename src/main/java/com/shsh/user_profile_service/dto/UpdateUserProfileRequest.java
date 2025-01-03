package com.shsh.user_profile_service.dto;

import com.shsh.user_profile_service.model.Gender;
import com.shsh.user_profile_service.model.UserProfile;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserProfileRequest {

	private String username;
	private String email;
	private String descriptionOfProfile;
	private String status;
	private String avatarUrl;
	private String chatWallpaperUrl;
	private Gender gender;
	private LocalDate dateOfBirth;

}
