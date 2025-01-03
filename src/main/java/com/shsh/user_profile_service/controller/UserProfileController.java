package com.shsh.user_profile_service.controller;

import com.shsh.user_profile_service.dto.CreateUserProfileRequest;
import com.shsh.user_profile_service.dto.PremiumStatusResponse;
import com.shsh.user_profile_service.dto.UpdateUserProfileRequest;
import com.shsh.user_profile_service.model.UserProfile;
import com.shsh.user_profile_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequestMapping("/user/profile")
@RequiredArgsConstructor
@Slf4j
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getProfileById(@PathVariable String id) {
        UserProfile profile = userProfileService.getProfileById(id);
        return ResponseEntity.ok(profile);
    }
    @GetMapping("/{id}/check-premium")
    public ResponseEntity<PremiumStatusResponse> checkPremiumStatus(@PathVariable String id) {
        log.info("Checking premium status for user with ID: {}", id);
        UserProfile profile = userProfileService.getProfileById(id);
        boolean isPremium = userProfileService.checkAndUpdatePremiumStatus(id);
        return ResponseEntity.ok(new PremiumStatusResponse(isPremium, profile.getPremiumExpiresAt()));
    }



    @PostMapping("/create")
    public ResponseEntity<UserProfile> createNewProfile(@RequestBody CreateUserProfileRequest request) {
        log.info("Received profile creation request: {}", request);
        UserProfile userProfile = userProfileService.createUserProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfile);
    }

    @PostMapping("/update")
    public ResponseEntity<UserProfile> updateUserProfile(@RequestBody UpdateUserProfileRequest request) {
        UserProfile userProfile = userProfileService.update(request);
        return ResponseEntity.status(HttpStatus.OK).body(userProfile);
    }


}
