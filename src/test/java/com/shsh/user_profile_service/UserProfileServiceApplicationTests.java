package com.shsh.user_profile_service;

import com.shsh.user_profile_service.controller.UserProfileController;
import com.shsh.user_profile_service.dto.CreateUserProfileRequest;
import com.shsh.user_profile_service.model.UserProfile;
import com.shsh.user_profile_service.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserProfileServiceApplicationTests {

	@MockBean
	private UserProfileService userProfileService;

	@Test
	void contextLoads() {
	}

	@Test
	void getProfileByIdTest(){

		CreateUserProfileRequest createUserProfileRequest = new CreateUserProfileRequest();
		createUserProfileRequest.setId("0");
		createUserProfileRequest.setUsername("USA");
		createUserProfileRequest.setEmail("danchick2005@gmail.com");
		userProfileService.createUserProfile(createUserProfileRequest);
		UserProfile response = userProfileService.getProfileById("0");

		assertEquals(createUserProfileRequest.getId(), response.getId());
		assertEquals(createUserProfileRequest.getUsername(), response.getUsername());
		assertEquals(createUserProfileRequest.getEmail(), response.getEmail());
		userProfileService.delete("0");
	}

}
