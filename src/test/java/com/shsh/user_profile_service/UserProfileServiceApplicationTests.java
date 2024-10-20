package com.shsh.user_profile_service;

import com.shsh.user_profile_service.controller.UserProfileController;
import com.shsh.user_profile_service.dto.CreateUserProfileRequest;
import com.shsh.user_profile_service.model.UserProfile;

import com.shsh.user_profile_service.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class UserProfileServiceApplicationTests {



	@Test
	void contextLoads() {
	}

}

}
