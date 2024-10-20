package com.shsh.user_profile_service;

import com.shsh.user_profile_service.model.UserProfile;
import com.shsh.user_profile_service.repository.UserProfileRepository;
import com.shsh.user_profile_service.service.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class GetProfileByIdTests {

	@Mock
	private UserProfileRepository userProfileRepository;

	@InjectMocks
	private UserProfileService userProfileService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

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
		assertEquals("", result.getDescriptionOfProfile());
		assertEquals("", result.getStatus());
	}

	@Test
	void getProfileByIdNotFoundTest(){

		when(userProfileRepository.findById("0")).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			userProfileService.getProfileById("0");
		});

		assertEquals("Profile not found", exception.getMessage());
	}

}
