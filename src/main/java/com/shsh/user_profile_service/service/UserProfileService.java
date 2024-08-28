package com.shsh.user_profile_service.service;

import com.shsh.user_profile_service.dto.CreateUserProfileRequest;
import com.shsh.user_profile_service.model.UserProfile;
import com.shsh.user_profile_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public List<UserProfile> findAll() {
        return userProfileRepository.findAll();
    }

    public UserProfile findById(String id) {
        return userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public UserProfile save(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public UserProfile update(String id, UserProfile userProfile) {
        UserProfile existingProfile = findById(id);
        existingProfile.setUsername(userProfile.getUsername());
        existingProfile.setEmail(userProfile.getEmail());
        existingProfile.setDescriptionOfProfile(userProfile.getDescriptionOfProfile());
        existingProfile.setStatus(userProfile.getStatus());
        return userProfileRepository.save(existingProfile);
    }

    public void delete(String id) {
        userProfileRepository.deleteById(id);
    }

    public UserProfile createUserProfile(CreateUserProfileRequest request) {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(request.getId());
        userProfile.setUsername(request.getUsername());
        userProfile.setEmail(request.getEmail());
        userProfile.setDescriptionOfProfile("");
        userProfile.setStatus("");
        return userProfileRepository.save(userProfile);
    }
}