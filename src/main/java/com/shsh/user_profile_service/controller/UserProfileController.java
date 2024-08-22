package com.shsh.user_profile_service.controller;

import com.shsh.user_profile_service.model.UserProfile;
import com.shsh.user_profile_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
        List<UserProfile> profiles = userProfileService.findAll();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getProfileById(@PathVariable UUID id, @RequestHeader HttpHeaders headers) {
        System.out.println("Received Headers: " + headers);
        UserProfile profile = userProfileService.findById(id);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/profile")
    public ResponseEntity<UserProfile> createProfile(@RequestBody UserProfile userProfile) {
        UserProfile createdProfile = userProfileService.save(userProfile);
        return ResponseEntity.ok(createdProfile);
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<UserProfile> updateProfile(@PathVariable UUID id, @RequestBody UserProfile userProfile) {
        UserProfile updatedProfile = userProfileService.update(id, userProfile);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/profile/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable UUID id) {
        userProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
