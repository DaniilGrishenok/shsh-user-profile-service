package com.shsh.user_profile_service.controller;

import com.shsh.user_profile_service.dto.CreateUserProfileRequest;
import com.shsh.user_profile_service.model.UserProfile;
import com.shsh.user_profile_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/profiles")
    public ResponseEntity<List<UserProfile>> getAllProfiles() {
        List<UserProfile> profiles = userProfileService.getAllProfiles();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getProfileById(@PathVariable String id, @RequestHeader HttpHeaders headers) {
        System.out.println("Received Headers: " + headers);
        UserProfile profile = userProfileService.getProfileById(id);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/create")
    public ResponseEntity<UserProfile> createNewProfile(@RequestBody CreateUserProfileRequest request) {
        UserProfile userProfile = userProfileService.createUserProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfile);
    }


}
