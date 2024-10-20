package com.shsh.user_profile_service;

import com.shsh.user_profile_service.controller.UserProfileController;
import com.shsh.user_profile_service.dto.CreateUserProfileRequest;
import com.shsh.user_profile_service.model.UserProfile;
import com.shsh.user_profile_service.repository.UserProfileRepository;
import com.shsh.user_profile_service.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserProfileServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}

@SpringBootTest
class UserProfileServiceGetProfileByIdTests {

	@MockBean
	private UserProfileRepository userProfileRepository;

	@InjectMocks
	private UserProfileService userProfileService;

	@Test
	void getProfileByIdSuccesTest(){

		UserProfile userProfile = new UserProfile();
		userProfile.setId("0");
		userProfile.setUsername("USA");
		userProfile.setEmail("danchick2005@gmail.com");
		userProfile.setDescriptionOfProfile("");
		userProfile.setStatus("");

		when(userProfileRepository.findById("0")).thenReturn(Optional.of(userProfile));

		UserProfile result = userProfileService.getProfileById("0");

		assertEquals("0", result.getId());
		assertEquals("USA", result.getUsername());
		assertEquals("danchick2005@gmail.com", result.getEmail());
	}

	void getProfileByIdNotFoundTest(){

		when(userProfileRepository.findById("0")).thenReturn(Optional.of(null));

		UserProfile result = userProfileService.getProfileById("0");

		assertEquals(null, result);
	}

}
